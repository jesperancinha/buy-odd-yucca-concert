package org.jesperancinha.concert.buy.oyc.ticket.rest

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
import kotlinx.coroutines.flow.map
import org.jesperancinha.concert.buy.oyc.commons.dto.ResponseDto
import org.jesperancinha.concert.buy.oyc.commons.dto.TicketDto
import org.jesperancinha.concert.buy.oyc.commons.dto.toDto
import org.jesperancinha.concert.buy.oyc.ticket.service.TicketService
import javax.validation.Valid

@DelicateCoroutinesApi
@Controller("/api")
class TicketController(
    private val ticketService: TicketService
) {
    @Post
    suspend fun saveTicket(@Body ticketDto: @Valid TicketDto?): ResponseDto =
        ticketDto?.let {
            ticketService.createTicket(ticketDto)
            ResponseDto(code= HttpStatus.CREATED.code, message =  "Saved successfully !")
        } ?: ResponseDto(code = HttpStatus.NOT_FOUND.code)

    @Get(value = "/", produces = [MediaType.APPLICATION_JSON])
    fun getAllTickets(): Flow<TicketDto> =
        ticketService.getAll().map { it.toDto }
}