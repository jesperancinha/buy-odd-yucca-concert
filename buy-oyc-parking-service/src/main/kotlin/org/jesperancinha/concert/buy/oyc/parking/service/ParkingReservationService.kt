package org.jesperancinha.concert.buy.oyc.parking.service

import io.lettuce.core.RedisClient
import io.lettuce.core.pubsub.RedisPubSubAdapter
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.jesperancinha.concert.buy.oyc.commons.domain.*
import org.jesperancinha.concert.buy.oyc.commons.dto.ParkingReservationDto
import org.jesperancinha.concert.buy.oyc.commons.dto.toData
import org.jesperancinha.concert.buy.oyc.commons.dto.toDto
import org.jesperancinha.concert.buy.oyc.commons.pubsub.initPubSub
import java.io.ObjectInputStream
import javax.validation.Valid

private const val PARKING_CHANNEL = "parkingChannel"

/**
 * Created by jofisaes on 20/04/2022
 */
@DelicateCoroutinesApi
@Singleton
class ParkingReservationService(
    private val parkingRepository: ParkingReservationRepository,
    private val carParkingRepository: CarParkingRepository,
    redisClient: RedisClient,
    private val pubSubCommands: RedisPubSubAsyncCommands<String, ParkingReservationDto>,
) {

    init {
        redisClient.initPubSub(
            channelName = PARKING_CHANNEL,
            redisCodec = ParkingReservationCodec(),
            redisPubSubAdapter = Listener(
                parkingRepository,
                carParkingRepository
            )
        )
    }

    suspend fun createParkingReservation(parkingReservationDto: @Valid ParkingReservationDto?): Unit =
        withContext(Dispatchers.Default) {
            pubSubCommands.publish(PARKING_CHANNEL, parkingReservationDto)
        }

    fun getAll(): Flow<ParkingReservationDto> = parkingRepository.findAll().map { it.toDto }
}

@Factory
class RedisBeanFactory {
    @Singleton
    fun pubSubCommands(redisClient: RedisClient): RedisPubSubAsyncCommands<String, ParkingReservationDto>? =
        redisClient.connectPubSub(ParkingReservationCodec()).async()
}

@DelicateCoroutinesApi
class Listener(
    private val parkingRepository: ParkingReservationRepository,
    private val carParkingRepository: CarParkingRepository
) : RedisPubSubAdapter<String, ParkingReservationDto>() {
    override fun message(key: String, parkingReservationDto: ParkingReservationDto) {
        val parkingReservation =
            parkingReservationDto.toData(carParkingRepository.findByParkingNumber(parkingReservationDto.carParkingId))
        CoroutineScope(Dispatchers.IO).launch {
            parkingRepository.save(parkingReservation)
        }
    }

}

class ParkingReservationCodec : BuyOycCodec<ParkingReservationDto>() {
    override fun readCodecObject(it: ObjectInputStream): ParkingReservationDto = it.readTypedObject()
}
