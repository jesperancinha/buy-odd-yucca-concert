package org.jesperancinha.concert.buy.oyc.ticket.service

import io.lettuce.core.RedisClient
import io.lettuce.core.pubsub.RedisPubSubAdapter
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value
import io.micronaut.rxjava3.http.client.Rx3HttpClient
import jakarta.inject.Qualifier
import jakarta.inject.Singleton
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import org.jesperancinha.concert.buy.oyc.commons.domain.*
import org.jesperancinha.concert.buy.oyc.commons.dto.TicketDto
import org.jesperancinha.concert.buy.oyc.commons.dto.toTicketData
import org.jesperancinha.concert.buy.oyc.commons.pubsub.initPubSub
import org.jesperancinha.concert.buy.oyc.commons.rest.sendObject
import java.io.ObjectInputStream
import java.net.URL
import javax.validation.Valid
import kotlin.annotation.AnnotationRetention.RUNTIME

private const val TICKET_PERSIST_CHANNEL = "ticketPersistChannel"

/**
 * Created by jofisaes on 30/03/2022
 */
@DelicateCoroutinesApi
@Singleton
class TicketService(
    private val ticketReservationRepository: TicketReservationRepository,
    auditLogRepository: AuditLogRepository,
    private val pubSubCommands: RedisPubSubAsyncCommands<String, TicketDto>,
    redisClient: RedisClient,
    @ConcertClient
    httpConcertClient: Rx3HttpClient,
    @ParkingClient
    httpParkingClient: Rx3HttpClient,
    @CateringClient
    httpCateringClient: Rx3HttpClient,
    ticketServiceHttpConfiguration: TicketServiceHttpConfiguration
) {

    init {
        redisClient.initPubSub(
            channelName = TICKET_PERSIST_CHANNEL,
            redisCodec = TicketCodec(),
            redisPubSubAdapter = Listener(
                ticketServiceHttpConfiguration,
                auditLogRepository,
                httpConcertClient,
                httpParkingClient,
                httpCateringClient,
                ticketReservationRepository
            )
        )
    }

    suspend fun createTicket(ticketDto: @Valid TicketDto): Unit = withContext(Dispatchers.Default) {
        pubSubCommands.publish(TICKET_PERSIST_CHANNEL, ticketDto)
    }

    fun getAll(): Flow<TicketReservation> = ticketReservationRepository.findAll()
}

@Factory
class RedisBeanFactory {
    @Singleton
    fun pubSubCommands(redisClient: RedisClient): RedisPubSubAsyncCommands<String, TicketDto> =
        redisClient.connectPubSub(TicketCodec()).async()

    @Singleton
    @ConcertClient
    fun httpConcertClient(
        @Value("\${buy.oyc.concert.host}")
        host: String,
        @Value("\${buy.oyc.concert.port}")
        port: Long
    ): Rx3HttpClient =
        Rx3HttpClient.create(URL("http://" + host + ":" + port))

    @Singleton
    @ParkingClient
    fun httpParkingClient(
        @Value("\${buy.oyc.parking.host}")
        host: String,
        @Value("\${buy.oyc.parking.port}")
        port: Long
    ): Rx3HttpClient =
        Rx3HttpClient.create(URL("http://" + host + ":" + port))

    @Singleton
    @CateringClient
    fun httpCateringClient(
        @Value("\${buy.oyc.catering.host}")
        host: String,
        @Value("\${buy.oyc.catering.port}")
        port: Long
    ): Rx3HttpClient =
        Rx3HttpClient.create(URL("http://" + host + ":" + port))
}

@DelicateCoroutinesApi
class Listener(
    private val ticketServiceHttpConfiguration: TicketServiceHttpConfiguration,
    private val auditLogRepository: AuditLogRepository,
    private val httpConcertClient: Rx3HttpClient,
    private val httpParkingClient: Rx3HttpClient,
    private val httpCateringClient: Rx3HttpClient,
    private val ticketReservationRepository: TicketReservationRepository,
) : RedisPubSubAdapter<String, TicketDto>(), TicketServiceHttpConfigurationInterface by ticketServiceHttpConfiguration {
    override fun message(key: String, ticketDto: TicketDto) {
        val ticketData = ticketDto.toTicketData
        CoroutineScope(Dispatchers.IO).launch {
            val ticketReservation = ticketReservationRepository.save(ticketData)
            ticketDto.drinks.forEach {
                httpCateringClient.sendObject(
                    it.apply {
                        reference = ticketDto.reference
                        ticketReservationId = ticketReservation.id
                    },
                    cateringDrinkUrl,
                    auditLogRepository
                )
            }
            ticketDto.meals.forEach {
                httpCateringClient.sendObject(
                    it.apply {
                        reference = ticketDto.reference
                        ticketReservationId = ticketReservation.id
                    },
                    cateringMealUrl,
                    auditLogRepository
                )
            }
            ticketDto.concertDays.forEach {
                httpConcertClient.sendObject(
                    it.apply {
                        reference = ticketDto.reference
                    },
                    concertUrl,
                    auditLogRepository
                )
            }
            ticketDto.parkingReservation?.let {
                httpParkingClient.sendObject(
                    it.apply { reference = ticketDto.reference },
                    ticketServiceHttpConfiguration.parkingUrl,
                    auditLogRepository
                )
            }
        }
    }

}

interface TicketServiceHttpConfigurationInterface {
    val cateringDrinkUrl: String
    val cateringMealUrl: String
    val concertUrl: String
    val parkingUrl: String
}

class TicketCodec : BuyOycCodec<TicketDto>() {
    override fun readCodecObject(it: ObjectInputStream): TicketDto = it.readTypedObject()
}

@Singleton
data class TicketServiceHttpConfiguration(
    @Value("\${buy.oyc.catering.url.drink}")
    override val cateringDrinkUrl: String,
    @Value("\${buy.oyc.catering.url.meal}")
    override val cateringMealUrl: String,
    @Value("\${buy.oyc.concert.url}")
    override val concertUrl: String,
    @Value("\${buy.oyc.parking.url}")
    override val parkingUrl: String,
) : TicketServiceHttpConfigurationInterface

@Qualifier
@Retention(RUNTIME)
annotation class ConcertClient

@Qualifier
@Retention(RUNTIME)
annotation class ParkingClient

@Qualifier
@Retention(RUNTIME)
annotation class CateringClient