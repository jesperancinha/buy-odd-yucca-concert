package org.jesperancinha.concert.buy.oyc.ticket.dto

import org.jesperancinha.concert.buy.oyc.commons.domain.TicketReservation
import java.time.LocalDate
import java.util.*

data class TicketDto(
    val tN: UUID,
    val name: String,
    val address: String,
    val birthDate: LocalDate

)

val TicketReservation.toDto: TicketDto
    get() = TicketDto(
        tN = reference,
        name = name,
        address = address,
        birthDate = birthDate
    )

val TicketDto.toData: TicketReservation
    get() = TicketReservation(
        reference = tN,
        name = name,
        address = address,
        birthDate = birthDate,
        meals = emptyList()
    )
