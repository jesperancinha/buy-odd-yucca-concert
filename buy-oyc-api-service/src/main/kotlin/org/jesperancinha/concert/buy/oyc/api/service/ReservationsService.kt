package org.jesperancinha.concert.buy.oyc.api.service

import io.lettuce.core.RedisClient
import io.lettuce.core.pubsub.RedisPubSubAdapter
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value
import io.micronaut.http.HttpRequest
import io.micronaut.rxjava3.http.client.Rx3StreamingHttpClient
import io.reactivex.rxjava3.core.Single
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import org.jesperancinha.concert.buy.oyc.api.dto.ReceiptDto
import org.jesperancinha.concert.buy.oyc.api.dto.TicketDto
import org.jesperancinha.concert.buy.oyc.api.dto.toDto
import org.jesperancinha.concert.buy.oyc.commons.domain.BuyOycCodec
import org.jesperancinha.concert.buy.oyc.commons.domain.Receipt
import org.jesperancinha.concert.buy.oyc.commons.domain.ReceiptRepository
import org.jesperancinha.concert.buy.oyc.commons.domain.readTypedObject
import java.io.ObjectInputStream
import java.net.URL
import javax.validation.Valid


/**
 * Created by jofisaes on 30/03/2022
 */
@Singleton
class ReservationsService(
    private val receiptRepository: ReceiptRepository,
    private val pubSubCommands: RedisPubSubAsyncCommands<String, TicketDto>,
    redisClient: RedisClient,
    @Value("\${buy.oyc.ticket.host}")
    val host: String,
    @Value("\${buy.oyc.ticket.url}")
    val url: String,
    @Value("\${buy.oyc.ticket.port}")
    val port: Long
) {

    init {
        val statefulRedisPubSubConnection = redisClient.connectPubSub(TicketCodec())
        statefulRedisPubSubConnection.addListener(Listener(host, url, port))
        val redisPubSubAsyncCommands = statefulRedisPubSubConnection.async()
        redisPubSubAsyncCommands.subscribe("ticketsChannel")
    }

    suspend fun createTicket(ticketDto: @Valid TicketDto): ReceiptDto {
        val save = receiptRepository.save(Receipt())
        val receiptDto = save.toDto
        pubSubCommands.publish("ticketsChannel", ticketDto.copy(reference = receiptDto.reference))
        return receiptDto
    }

    fun getAll(): Flow<Receipt> = receiptRepository.findAll()
}

@Factory
class RedisBeanFactory {
    @Singleton
    fun pubSubCommands(redisClient: RedisClient): RedisPubSubAsyncCommands<String, TicketDto> =
        redisClient.connectPubSub(TicketCodec()).async()
}

class Listener(
    private val host: String,
    private val url: String,
    private val port: Long
) : RedisPubSubAdapter<String, TicketDto>() {
    override fun message(key: String, ticketDto: TicketDto) {
        println(key)
        println(ticketDto)
        val client: Rx3StreamingHttpClient =
            Rx3StreamingHttpClient.create(URL("http://" + host + ":" + port))
        val s: Single<TicketDto> =
            client.retrieve(HttpRequest.POST(url, ticketDto), TicketDto::class.java).firstOrError()
        s.subscribe()
        print(s.blockingGet())
    }
}

class TicketCodec : BuyOycCodec<TicketDto>() {
    override fun readCodecObject(it: ObjectInputStream): TicketDto = it.readTypedObject()
}