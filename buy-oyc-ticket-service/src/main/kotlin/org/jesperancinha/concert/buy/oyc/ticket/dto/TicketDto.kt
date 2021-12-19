package org.jesperancinha.concert.buy.oyc.ticket.dto

import org.jesperancinha.concert.buy.oyc.commons.domain.ParkingReservation
import org.jesperancinha.concert.buy.oyc.commons.domain.Ticket
import java.util.*

data class TicketDto(
    val tN: UUID?
)

val Ticket.toDto: TicketDto
    get() = TicketDto(tN = id)

val TicketDto.toData: Ticket
    get() = Ticket(id = tN)
