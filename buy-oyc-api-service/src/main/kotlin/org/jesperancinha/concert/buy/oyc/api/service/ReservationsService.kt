package org.jesperancinha.concert.buy.oyc.api.service

import io.lettuce.core.RedisClient
import io.lettuce.core.codec.ByteArrayCodec
import io.lettuce.core.codec.RedisCodec
import io.lettuce.core.pubsub.RedisPubSubAdapter
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.Flow
import org.jesperancinha.concert.buy.oyc.api.dto.ReceiptDto
import org.jesperancinha.concert.buy.oyc.api.dto.TicketDto
import org.jesperancinha.concert.buy.oyc.api.dto.toDto
import org.jesperancinha.concert.buy.oyc.commons.domain.Receipt
import org.jesperancinha.concert.buy.oyc.commons.domain.ReceiptRepository
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.nio.ByteBuffer
import java.nio.charset.Charset.defaultCharset
import javax.validation.Valid


/**
 * Created by jofisaes on 30/03/2022
 */
@Singleton
class ReservationsService(
    private val receiptRepository: ReceiptRepository,
    private val redisClient: RedisClient,
    val pubSubCommands: RedisPubSubAsyncCommands<String, String>
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


class TicketCodec : RedisCodec<String, TicketDto> {

    override fun decodeKey(byteBuffer: ByteBuffer): String {
        return defaultCharset().decode(byteBuffer).toString()
    }

    override fun decodeValue(byteBuffer: ByteBuffer): TicketDto =
        ObjectInputStream(
            ByteArrayInputStream(byteArrayCodec.decodeValue(byteBuffer))
        ).use { it.readObject() as TicketDto }

    override fun encodeKey(key: String): ByteBuffer {
        return defaultCharset().encode(key)
    }

    override fun encodeValue(ticketDto: TicketDto): ByteBuffer =
        ByteArrayOutputStream().use { baos ->
            ObjectOutputStream(baos).use { oos ->
                oos.writeObject(ticketDto)
                byteArrayCodec.encodeValue(baos.toByteArray())
            }
        }

    companion object {
        val byteArrayCodec = ByteArrayCodec()
    }

}