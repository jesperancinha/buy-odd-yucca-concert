package org.jesperancinha.concert.buy.oyc.parking.rest

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import kotlinx.coroutines.flow.Flow
import org.jesperancinha.concert.buy.oyc.commons.domain.ParkingReservation
import org.jesperancinha.concert.buy.oyc.commons.domain.ParkingReservationRepository

@Controller("/")
open class WelcomeController(
    private val parkingReservationRepository: ParkingReservationRepository,
) {
    @Get(value = "/", produces = [MediaType.APPLICATION_JSON])
    open suspend fun getWelcomeMessage() = "message" to "Welcome to the Parking service"

    @Get(value = "/test", produces = [MediaType.APPLICATION_JSON])
    open fun getAllParkingReservations(): Flow<ParkingReservation> {
        return parkingReservationRepository.findAll()
    }
}