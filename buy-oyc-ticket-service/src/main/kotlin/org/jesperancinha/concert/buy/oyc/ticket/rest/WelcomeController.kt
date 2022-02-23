package org.jesperancinha.concert.buy.oyc.ticket.rest

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import kotlinx.coroutines.flow.Flow
import org.jesperancinha.concert.buy.oyc.commons.domain.TicketRepository
import org.jesperancinha.concert.buy.oyc.commons.domain.TicketReservation

@Controller("/")
open class WelcomeController(
    private val ticketRepository: TicketRepository,
) {
    @Get(value = "/", produces = [MediaType.APPLICATION_JSON])
    open suspend fun getWelcomeMessage() = "message" to "Welcome to the Ticket service"

    @Get(value = "/test", produces = [MediaType.APPLICATION_JSON])
    open fun getAllParkingReservations(): Flow<TicketReservation> {
        return ticketRepository.findAll()
    }
}