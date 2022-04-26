package org.jesperancinha.concert.buy.oyc.parking.rest

import io.micronaut.http.HttpResponse.status
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.Flow
import org.jesperancinha.concert.buy.oyc.commons.dto.ParkingReservationDto
import org.jesperancinha.concert.buy.oyc.commons.dto.ResponseDto
import org.jesperancinha.concert.buy.oyc.parking.service.ParkingReservationService
import javax.validation.Valid

@Controller("/api")
@DelicateCoroutinesApi
class ParkingController(
    private val parkingReservationService: ParkingReservationService
) {
    @Post
    suspend fun createParkingReservation(@Body parkingReservation: @Valid ParkingReservationDto?): ResponseDto =
        parkingReservation?.let {
            parkingReservationService.createParkingReservation(parkingReservation)
            ResponseDto(code= HttpStatus.CREATED.code, message =  "Saved successfully !")
        } ?: ResponseDto(code = HttpStatus.NOT_FOUND.code)

    @Get(value = "/", produces = [MediaType.APPLICATION_JSON])
    fun getAllParkingReservations(): Flow<ParkingReservationDto> {
        return parkingReservationService.getAll()
    }
}