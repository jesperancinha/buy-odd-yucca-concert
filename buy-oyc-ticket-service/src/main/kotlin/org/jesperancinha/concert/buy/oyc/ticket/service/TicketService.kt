package org.jesperancinha.concert.buy.oyc.ticket.service

import io.lettuce.core.RedisClient
import io.lettuce.core.pubsub.RedisPubSubAdapter
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value
import io.micronaut.http.HttpRequest
import io.micronaut.rxjava3.http.client.Rx3StreamingHttpClient
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.internal.schedulers.SingleScheduler
import jakarta.inject.Singleton
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import org.jesperancinha.concert.buy.oyc.commons.domain.*
import org.jesperancinha.concert.buy.oyc.commons.dto.TicketDto
import org.jesperancinha.concert.buy.oyc.commons.dto.toConcertData
import org.jesperancinha.concert.buy.oyc.commons.dto.toParkingData
import org.jesperancinha.concert.buy.oyc.commons.dto.toTicketData
import org.jesperancinha.concert.buy.oyc.commons.pubsub.initPubSub
import org.jesperancinha.concert.buy.oyc.commons.rest.sendObject
import java.io.ObjectInputStream
import java.net.URL
import javax.validation.Valid

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
    @Value("\${buy.oyc.ticket.url}")
    val url: String,
    httpClient: Rx3StreamingHttpClient
) {

    init {
        redisClient.initPubSub(
            channelName = TICKET_PERSIST_CHANNEL,
            redisCodec = TicketCodec(),
            redisPubSubAdapter = Listener(url, auditLogRepository, httpClient, ticketRepository)
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
    fun httpConcertClient(
        @Value("\${buy.oyc.concert.host}")
        host: String,
        @Value("\${buy.oyc.concert.port}")
        port: Long
    ): Rx3StreamingHttpClient =
        Rx3StreamingHttpClient.create(URL("http://" + host + ":" + port))

    @Singleton
    fun httpParkingClient(
        @Value("\${buy.oyc.parking.host}")
        host: String,
        @Value("\${buy.oyc.parking.port}")
        port: Long
    ): Rx3StreamingHttpClient =
        Rx3StreamingHttpClient.create(URL("http://" + host + ":" + port))
}

@DelicateCoroutinesApi
class Listener(
    private val url: String,
    private val auditLogRepository: AuditLogRepository,
    private val httpConcertClient: Rx3StreamingHttpClient,
    private val ticketRepository: TicketRepository,
) : RedisPubSubAdapter<String, TicketDto>() {
    override fun message(key: String, ticketDto: TicketDto) {
        val ticketData = ticketDto.toTicketData
        CoroutineScope(Dispatchers.IO).launch {
            ticketRepository.save(ticketData)
        }
        val concertDays = ticketDto.toConcertData
        val parkingReservation = ticketDto.toParkingData
        httpConcertClient.sendObject(ticketDto, url, auditLogRepository)
    }

}

class TicketCodec : BuyOycCodec<TicketDto>() {
    override fun readCodecObject(it: ObjectInputStream): TicketDto = it.readTypedObject()
}
