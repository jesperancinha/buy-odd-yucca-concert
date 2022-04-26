package org.jesperancinha.concert.buy.oyc.concert.rest

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
import org.jesperancinha.concert.buy.oyc.commons.dto.ConcertDayDto
import org.jesperancinha.concert.buy.oyc.commons.dto.ResponseDto
import org.jesperancinha.concert.buy.oyc.concert.service.ConcertDayService
import javax.validation.Valid

@DelicateCoroutinesApi
@Controller("/api")
class ConcertController(
    private val concertDayService: ConcertDayService
) {
    @Post
    suspend fun createConcertDay(@Body concertDayDto: @Valid ConcertDayDto?): ResponseDto =
        concertDayDto?.let {
            concertDayService.createConcertDayReservation(concertDayDto)
            ResponseDto(code= HttpStatus.CREATED.code, message =  "Saved successfully !")
        } ?: ResponseDto(code = HttpStatus.NOT_FOUND.code)

    @Get(value = "/", produces = [MediaType.APPLICATION_JSON])
    fun getAllParkingReservations(): Flow<ConcertDayDto> {
        return concertDayService.getAll()
    }
}