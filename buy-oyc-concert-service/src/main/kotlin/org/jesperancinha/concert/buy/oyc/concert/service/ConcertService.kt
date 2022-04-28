package org.jesperancinha.concert.buy.oyc.concert.service

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
import org.jesperancinha.concert.buy.oyc.commons.domain.ConcertDayReservationRepository
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
private const val CONCERT_CHANNEL = "concertChannel"

@DelicateCoroutinesApi
@Singleton
class ConcertDayService(
    private val concertDayRepository: ConcertDayRepository,
    private val concertDayReservationRepository: ConcertDayReservationRepository,
    redisClient: RedisClient,
    private val pubSubCommands: RedisPubSubAsyncCommands<String, ConcertDayDto>,
) {

    init {
        redisClient.initPubSub(
            channelName = CONCERT_CHANNEL,
            redisCodec = ConcertDayCodec(),
            redisPubSubAdapter = Listener(
                concertDayRepository,
                concertDayReservationRepository
            )
        )
    }

    suspend fun createConcertDayReservation(concertDayDto: @Valid ConcertDayDto?): Unit =
        withContext(Dispatchers.Default) {
            pubSubCommands.publish(CONCERT_CHANNEL, concertDayDto)
        }

    fun getAll(): Flow<ConcertDayDto> = concertDayReservationRepository.findAll().map { it.toDto }
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
    private val concertDayReservationRepository: ConcertDayReservationRepository,
) : RedisPubSubAdapter<String, ConcertDayDto>() {
    override fun message(key: String, concertDayDto: ConcertDayDto) {
        CoroutineScope(Dispatchers.IO).launch {
            val concertDayReservation =
                concertDayDto.toData(
                    concertDayRepository.findById(
                        concertDayDto.concertId ?: throw RuntimeException("Concert not sent!")
                    ) ?: throw RuntimeException("Concert Not found")
                )
            concertDayReservationRepository.save(concertDayReservation)
        }
    }

}

class ConcertDayCodec : BuyOycCodec<ConcertDayDto>() {
    override fun readCodecObject(it: ObjectInputStream): ConcertDayDto = it.readTypedObject()
}
