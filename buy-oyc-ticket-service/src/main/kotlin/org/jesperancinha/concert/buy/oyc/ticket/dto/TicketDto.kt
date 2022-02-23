package org.jesperancinha.concert.buy.oyc.ticket.dto

import org.jesperancinha.concert.buy.oyc.commons.domain.TicketReservation
import java.util.*

data class TicketDto(
    val tN: UUID?,
)

val TicketReservation.toDto: TicketDto
    get() = TicketDto(tN = id)

val TicketDto.toData: TicketReservation
    get() = TicketReservation(id = tN)
