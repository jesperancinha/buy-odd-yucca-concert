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
import org.jesperancinha.concert.buy.oyc.commons.domain.TicketReservation
import org.jesperancinha.concert.buy.oyc.commons.dto.TicketDto
import org.jesperancinha.concert.buy.oyc.ticket.service.TicketService
import javax.validation.Valid

@DelicateCoroutinesApi
@Controller("/api")
class TicketController(
    private val ticketService: TicketService
) {
    @Post
    suspend fun saveParkingReservation(@Body ticketDto: @Valid TicketDto?): MutableHttpResponse<Pair<Int, String>> =
        ticketDto?.let {
            ticketService.createTicket(ticketDto)
            status<Map<Int, String>>(HttpStatus.CREATED).body(HttpStatus.CREATED.code to "Saved successfully !")
        } ?: status(HttpStatus.NOT_FOUND)

    @Get(value = "/", produces = [MediaType.APPLICATION_JSON])
    fun getAllParkingReservations(): Flow<TicketReservation> = ticketService.getAll()
}