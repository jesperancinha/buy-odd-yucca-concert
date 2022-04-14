package org.jesperancinha.concert.buy.oyc.parking.rest

import io.micronaut.http.HttpResponse.status
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import kotlinx.coroutines.flow.Flow
import org.jesperancinha.concert.buy.oyc.commons.domain.ParkingReservation
import org.jesperancinha.concert.buy.oyc.commons.domain.ParkingReservationRepository
import org.jesperancinha.concert.buy.oyc.parking.dto.ParkingReservationDto
import org.jesperancinha.concert.buy.oyc.parking.dto.toData
import javax.validation.Valid

@Controller("/api")
class ParkingController(
    private val parkingReservationRepository: ParkingReservationRepository,
) {
    @Post
    suspend fun saveParkingReservation(@Body parkingReservation: @Valid ParkingReservationDto?): MutableHttpResponse<Pair<Int, String>> =
        parkingReservation?.let {
            parkingReservationRepository.save(parkingReservation.toData)
            status<Map<Int, String>>(HttpStatus.CREATED).body(HttpStatus.CREATED.code to "Saved successfully !")
        } ?: status(HttpStatus.NOT_FOUND)

    @Get(value = "/", produces = [MediaType.APPLICATION_JSON])
    fun getAllParkingReservations(): Flow<ParkingReservation> {
        return parkingReservationRepository.findAll()
    }
}