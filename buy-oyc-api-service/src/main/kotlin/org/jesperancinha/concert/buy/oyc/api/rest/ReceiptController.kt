package org.jesperancinha.concert.buy.oyc.api.rest

import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.jesperancinha.concert.buy.oyc.api.service.ReservationsService
import org.jesperancinha.concert.buy.oyc.commons.dto.ReceiptDto
import org.jesperancinha.concert.buy.oyc.commons.dto.ResponseDto
import org.jesperancinha.concert.buy.oyc.commons.dto.TicketDto
import org.jesperancinha.concert.buy.oyc.commons.dto.toDto
import javax.validation.Valid

@Controller("/api")
@DelicateCoroutinesApi
class ReceiptController(
    private val reservationsService: ReservationsService
) {
    @Post
    suspend fun postReservation(@Body ticketDto: @Valid TicketDto?): ResponseDto =
        ticketDto?.let {
            reservationsService.createTicket(ticketDto)
            ResponseDto(code = HttpStatus.CREATED.code, message = "Saved successfully !")
        } ?: ResponseDto(code = HttpStatus.NOT_FOUND.code)

    @Get(value = "/", produces = [MediaType.APPLICATION_JSON])
    fun getAllReservations(): Flow<ReceiptDto> = reservationsService.getAll().map { it.toDto }
}