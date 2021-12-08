package org.jesperancinha.concert.buy.oyc.parking.rest

import io.micronaut.http.HttpResponse.status
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import jakarta.inject.Inject
import org.jesperancinha.concert.buy.oyc.commons.domain.ParkingReservation
import org.jesperancinha.concert.buy.oyc.commons.domain.ParkingReservationRepository
import org.jesperancinha.concert.buy.oyc.parking.dto.ParkingReservationDto
import org.jesperancinha.concert.buy.oyc.parking.dto.toDto
import javax.validation.Valid

@Controller("/api")
class ParkingController(
    @Inject
    private val parkingReservationRepository: ParkingReservationRepository
) {

    @Post
    fun saveParkingReservation(@Body parkingReservation: @Valid ParkingReservation?): MutableHttpResponse<Pair<Int, String>> =
        parkingReservation?.let {
            parkingReservationRepository.save(parkingReservation)
            status<Map<Int, String>>(HttpStatus.CREATED).body(HttpStatus.CREATED.code to "Saved successfully !")
        } ?: status(HttpStatus.NOT_FOUND)

    @Get(value = "/", produces = [MediaType.APPLICATION_JSON])
    fun getAllParkingReservations(): List<ParkingReservationDto> {
        return listOf(ParkingReservation(1L, 10L).toDto)
    }
}