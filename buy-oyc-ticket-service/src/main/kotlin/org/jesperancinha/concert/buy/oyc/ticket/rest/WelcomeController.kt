package org.jesperancinha.concert.buy.oyc.ticket.rest

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.jesperancinha.concert.buy.oyc.commons.domain.TicketRepository
import org.jesperancinha.concert.buy.oyc.commons.domain.TicketReservation
import org.jesperancinha.concert.buy.oyc.ticket.dto.TicketDto
import org.jesperancinha.concert.buy.oyc.ticket.dto.toDto

@Controller("/")
open class WelcomeController(
    private val ticketRepository: TicketRepository,
) {
    @Get(value = "/", produces = [MediaType.APPLICATION_JSON])
    open suspend fun getWelcomeMessage() = "message" to "Welcome to the Ticket service"

    @Get(value = "/test", produces = [MediaType.APPLICATION_JSON])
    open fun getAllParkingReservations(): Flow<TicketDto> {
        return ticketRepository.findAll().map { it.toDto }
    }
}