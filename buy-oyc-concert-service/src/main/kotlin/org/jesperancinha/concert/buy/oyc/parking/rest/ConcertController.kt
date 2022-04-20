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
import org.jesperancinha.concert.buy.oyc.commons.dto.ConcertDayDto
import org.jesperancinha.concert.buy.oyc.parking.service.ConcertDayService
import javax.validation.Valid

@DelicateCoroutinesApi
@Controller("/api")
class ConcertController(
    private val concertDayService: ConcertDayService
) {
    @Post
    suspend fun createConcertDay(@Body concertDayDto: @Valid ConcertDayDto?): MutableHttpResponse<Pair<Int, String>> =
        concertDayDto?.let {
            concertDayService.createConcertDayReservation(concertDayDto)
            status<Map<Int, String>>(HttpStatus.CREATED).body(HttpStatus.CREATED.code to "Saved successfully !")
        } ?: status(HttpStatus.NOT_FOUND)

    @Get(value = "/", produces = [MediaType.APPLICATION_JSON])
    fun getAllParkingReservations(): Flow<ConcertDayDto> {
        return concertDayService.getAll()
    }
}