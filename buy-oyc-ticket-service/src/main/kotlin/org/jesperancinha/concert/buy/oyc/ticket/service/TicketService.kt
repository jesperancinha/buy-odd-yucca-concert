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
import org.jesperancinha.concert.buy.oyc.commons.dto.*
import org.jesperancinha.concert.buy.oyc.commons.pubsub.initPubSub
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
            redisPubSubAdapter = Listener(url, auditLogRepository, httpClient)
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
    fun httpClient(
        @Value("\${buy.oyc.ticket.host}")
        host: String,
        @Value("\${buy.oyc.ticket.port}")
        port: Long
    ): Rx3StreamingHttpClient =
        Rx3StreamingHttpClient.create(URL("http://" + host + ":" + port))
}

@DelicateCoroutinesApi
class Listener(
    private val url: String,
    private val auditLogRepository: AuditLogRepository,
    private val client: Rx3StreamingHttpClient
) : RedisPubSubAdapter<String, TicketDto>() {
    override fun message(key: String, ticketDto: TicketDto) {
        val ticketData = ticketDto.toTicketData
        val concertDays = ticketDto.toConcertData
        val parkingReservation = ticketDto.parkingReservation?.toParkingReservationData
        val ticketDtoSingle: Single<TicketDto> =
            client.retrieve(HttpRequest.POST(url, ticketDto), TicketDto::class.java).firstOrError()
        val singleScheduler = SingleScheduler()
        ticketDtoSingle.subscribeOn(singleScheduler).doOnSuccess {
            GlobalScope.launch {
                auditLogRepository.save(
                    AuditLog(
                        auditLogType = AuditLogType.TICKET,
                        payload = ticketDto.toString()
                    )
                )
            }

        }.subscribe()
        singleScheduler.start()
    }
}

class TicketCodec : BuyOycCodec<TicketDto>() {
    override fun readCodecObject(it: ObjectInputStream): TicketDto = it.readTypedObject()
}
