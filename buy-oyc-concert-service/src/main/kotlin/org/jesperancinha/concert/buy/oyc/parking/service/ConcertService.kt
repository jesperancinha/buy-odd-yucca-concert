package org.jesperancinha.concert.buy.oyc.parking.service

import io.lettuce.core.RedisClient
import io.lettuce.core.pubsub.RedisPubSubAdapter
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.jesperancinha.concert.buy.oyc.commons.domain.BuyOycCodec
import org.jesperancinha.concert.buy.oyc.commons.domain.ConcertDayRepository
import org.jesperancinha.concert.buy.oyc.commons.domain.readTypedObject
import org.jesperancinha.concert.buy.oyc.commons.dto.ConcertDayDto
import org.jesperancinha.concert.buy.oyc.commons.dto.toData
import org.jesperancinha.concert.buy.oyc.commons.dto.toDto
import org.jesperancinha.concert.buy.oyc.commons.pubsub.initPubSub
import java.io.ObjectInputStream
import javax.validation.Valid

/**
 * Created by jofisaes on 20/04/2022
 */
private const val PARKING_CHANNEL = "parkingChannel"

/**
 * Created by jofisaes on 20/04/2022
 */
@DelicateCoroutinesApi
@Singleton
class ConcertDayService(
    private val concertDayRepository: ConcertDayRepository,
    redisClient: RedisClient,
    private val pubSubCommands: RedisPubSubAsyncCommands<String, ConcertDayDto>,
) {

    init {
        redisClient.initPubSub(
            channelName = PARKING_CHANNEL,
            redisCodec = ConcertDayCodec(),
            redisPubSubAdapter = Listener(
                concertDayRepository
            )
        )
    }

    suspend fun createConcertDayReservation(concertDayDto: @Valid ConcertDayDto?): Unit =
        withContext(Dispatchers.Default) {
            pubSubCommands.publish(PARKING_CHANNEL, concertDayDto)
        }

    fun getAll(): Flow<ConcertDayDto> = concertDayRepository.findAll().map { it.toDto }
}

@Factory
class RedisBeanFactory {
    @Singleton
    fun pubSubCommands(redisClient: RedisClient): RedisPubSubAsyncCommands<String, ConcertDayDto>? =
        redisClient.connectPubSub(ConcertDayCodec()).async()
}

@DelicateCoroutinesApi
class Listener(
    private val concertDayRepository: ConcertDayRepository,
) : RedisPubSubAdapter<String, ConcertDayDto>() {
    override fun message(key: String, concertDayDto: ConcertDayDto) {
        val parkingReservation =
            concertDayDto.toData
        CoroutineScope(Dispatchers.IO).launch {
            concertDayRepository.save(parkingReservation)
        }
    }

}

class ConcertDayCodec : BuyOycCodec<ConcertDayDto>() {
    override fun readCodecObject(it: ObjectInputStream): ConcertDayDto = it.readTypedObject()
}
