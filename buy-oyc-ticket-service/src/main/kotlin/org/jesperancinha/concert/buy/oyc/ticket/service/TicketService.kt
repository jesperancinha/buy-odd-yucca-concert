package org.jesperancinha.concert.buy.oyc.ticket.service

import io.lettuce.core.RedisClient
import io.lettuce.core.pubsub.RedisPubSubAdapter
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value
import io.micronaut.rxjava3.http.client.Rx3StreamingHttpClient
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
    private val ticketRepository: TicketRepository,
    auditLogRepository: AuditLogRepository,
    private val pubSubCommands: RedisPubSubAsyncCommands<String, TicketDto>,
    redisClient: RedisClient,
    @ConcertClient
    httpConcertClient: Rx3StreamingHttpClient,
    @ParkingClient
    httpParkingClient: Rx3StreamingHttpClient,
    @CateringClient
    httpCateringClient: Rx3StreamingHttpClient,
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
                ticketRepository
            )
        )
    }

    suspend fun createTicket(ticketDto: @Valid TicketDto): Unit = withContext(Dispatchers.Default) {
        pubSubCommands.publish(TICKET_PERSIST_CHANNEL, ticketDto)
    }

    fun getAll(): Flow<TicketReservation> = ticketRepository.findAll()
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
    ): Rx3StreamingHttpClient =
        Rx3StreamingHttpClient.create(URL("http://" + host + ":" + port))

    @Singleton
    @ParkingClient
    fun httpParkingClient(
        @Value("\${buy.oyc.parking.host}")
        host: String,
        @Value("\${buy.oyc.parking.port}")
        port: Long
    ): Rx3StreamingHttpClient =
        Rx3StreamingHttpClient.create(URL("http://" + host + ":" + port))

    @Singleton
    @CateringClient
    fun httpCateringClient(
        @Value("\${buy.oyc.catering.host}")
        host: String,
        @Value("\${buy.oyc.catering.port}")
        port: Long
    ): Rx3StreamingHttpClient =
        Rx3StreamingHttpClient.create(URL("http://" + host + ":" + port))
}

@DelicateCoroutinesApi
class Listener(
    private val ticketServiceHttpConfiguration: TicketServiceHttpConfiguration,
    private val auditLogRepository: AuditLogRepository,
    private val httpConcertClient: Rx3StreamingHttpClient,
    private val httpParkingClient: Rx3StreamingHttpClient,
    private val httpCateringClient: Rx3StreamingHttpClient,
    private val ticketRepository: TicketRepository,
) : RedisPubSubAdapter<String, TicketDto>() {
    override fun message(key: String, ticketDto: TicketDto) {
        val ticketData = ticketDto.toTicketData
        CoroutineScope(Dispatchers.IO).launch {
            ticketRepository.save(ticketData)
        }

        ticketDto.drinks.forEach {
            httpCateringClient.sendObject(
                it.apply { reference = ticketDto.reference },
                ticketServiceHttpConfiguration.cateringDrinkUrl,
                auditLogRepository
            )
        }
        ticketDto.meals.forEach {
            httpCateringClient.sendObject(
                it.apply { reference = ticketDto.reference },
                ticketServiceHttpConfiguration.cateringMealUrl,
                auditLogRepository
            )
        }
        ticketDto.concertDays.forEach {
            httpConcertClient.sendObject(
                it.apply { reference = ticketDto.reference },
                ticketServiceHttpConfiguration.concertUrl,
                auditLogRepository
            )
        }
        ticketDto.parkingReservation?.let {
            httpParkingClient.sendObject(
                it.apply { reference = ticketDto.reference },
                ticketServiceHttpConfiguration.concertUrl,
                auditLogRepository
            )
        }
    }

}

class TicketCodec : BuyOycCodec<TicketDto>() {
    override fun readCodecObject(it: ObjectInputStream): TicketDto = it.readTypedObject()
}

@Singleton
data class TicketServiceHttpConfiguration(
    @Value("\${buy.oyc.catering.url.drink}")
    val cateringDrinkUrl: String,
    @Value("\${buy.oyc.catering.url.meal}")
    val cateringMealUrl: String,
    @Value("\${buy.oyc.concert.url}")
    val concertUrl: String,
    @Value("\${buy.oyc.parking.url}")
    val parkingUrl: String,
)

@Qualifier
@Retention(RUNTIME)
annotation class ConcertClient

@Qualifier
@Retention(RUNTIME)
annotation class ParkingClient

@Qualifier
@Retention(RUNTIME)
annotation class CateringClient