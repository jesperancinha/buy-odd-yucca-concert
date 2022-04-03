package org.jesperancinha.concert.buy.oyc.api.service

import io.lettuce.core.RedisClient
import io.lettuce.core.pubsub.RedisPubSubAdapter
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.Flow
import org.jesperancinha.concert.buy.oyc.api.dto.ReceiptDto
import org.jesperancinha.concert.buy.oyc.api.dto.TicketDto
import org.jesperancinha.concert.buy.oyc.api.dto.toDto
import org.jesperancinha.concert.buy.oyc.commons.domain.BuyOycCodec
import org.jesperancinha.concert.buy.oyc.commons.domain.Receipt
import org.jesperancinha.concert.buy.oyc.commons.domain.ReceiptRepository
import java.io.ObjectInputStream
import javax.validation.Valid


/**
 * Created by jofisaes on 30/03/2022
 */
@Singleton
class ReservationsService(
    private val receiptRepository: ReceiptRepository,
    private val redisClient: RedisClient,
    val pubSubCommands: RedisPubSubAsyncCommands<String, TicketDto>
) {

    init {
        val statefulRedisPubSubConnection = redisClient.connectPubSub(TicketCodec())
        statefulRedisPubSubConnection.addListener(Listener())
        val redisPubSubAsyncCommands = statefulRedisPubSubConnection.async()
        redisPubSubAsyncCommands.subscribe("ticketsChannel")
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun createTicket(ticketDto: @Valid TicketDto): ReceiptDto {
//        val receiptDto = receiptRepository.save(Receipt()).toDto
        redisClient.connectPubSub(TicketCodec()).async().publish("ticketsChannel", ticketDto)
        return Receipt().toDto
    }

    fun getAll(): Flow<Receipt> = receiptRepository.findAll()
}

@Factory
class RedisBeanFactory {
    @Singleton
    fun pubSubCommands(redisClient: RedisClient): RedisPubSubAsyncCommands<String, TicketDto> =
        redisClient.connectPubSub(TicketCodec()).async()
}

class Listener : RedisPubSubAdapter<String, TicketDto>() {
    override fun message(key: String, ticketDto: TicketDto) {
        println(key)
        println(ticketDto)
    }
}


class TicketCodec : BuyOycCodec<TicketDto>() {
    override fun readCodecObject(it: ObjectInputStream): TicketDto = it.readTypedObject()
}