package org.jesperancinha.concert.buy.oyc.ticket.rest

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.jesperancinha.concert.buy.oyc.commons.domain.TicketReservationRepository
import org.jesperancinha.concert.buy.oyc.commons.dto.TicketDto
import org.jesperancinha.concert.buy.oyc.commons.dto.toDto

@Controller("/")
open class WelcomeController(
    private val ticketReservationRepository: TicketReservationRepository,
) {
    @Get(value = "/", produces = [MediaType.APPLICATION_JSON])
    open suspend fun getWelcomeMessage() = "message" to "Welcome to the Ticket service"

    @Get(value = "/test", produces = [MediaType.APPLICATION_JSON])
    open fun getAllParkingReservations(): Flow<TicketDto> {
        return ticketReservationRepository.findAll().map { it.toDto }
    }
}