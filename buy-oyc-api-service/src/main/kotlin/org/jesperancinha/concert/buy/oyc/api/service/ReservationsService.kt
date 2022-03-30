package org.jesperancinha.concert.buy.oyc.api.service

import io.lettuce.core.RedisClient
import io.lettuce.core.pubsub.RedisPubSubAdapter
import jakarta.inject.Singleton
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.jesperancinha.concert.buy.oyc.api.dto.TicketDto
import org.jesperancinha.concert.buy.oyc.api.dto.toDto
import org.jesperancinha.concert.buy.oyc.commons.domain.Receipt
import org.jesperancinha.concert.buy.oyc.commons.domain.ReceiptRepository
import javax.validation.Valid

/**
 * Created by jofisaes on 30/03/2022
 */
@Singleton
class ReservationsService(
    private val receiptRepository: ReceiptRepository,
    private val redisClient: RedisClient
) {
    init {
        val statefulRedisPubSubConnection = redisClient.connectPubSub()
        statefulRedisPubSubConnection.addListener(Listener())
        val redisPubSubAsyncCommands = statefulRedisPubSubConnection.async()
        redisPubSubAsyncCommands.subscribe("channel1")
        redisPubSubAsyncCommands.flushall()
        redisClient.connectPubSub().async().publish("channel1", "test")
        redisPubSubAsyncCommands.exec()
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun createTicket(ticketDto: @Valid TicketDto) = GlobalScope.launch {
        receiptRepository.save(Receipt()).toDto
    }

    fun getAll(): Flow<Receipt> = receiptRepository.findAll()
}

class Listener : RedisPubSubAdapter<String, String>() {
    override fun message(p0: String?, p1: String?) {
    }
}
