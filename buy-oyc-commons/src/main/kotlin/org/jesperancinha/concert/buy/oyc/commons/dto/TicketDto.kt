package org.jesperancinha.concert.buy.oyc.commons.dto

import org.jesperancinha.concert.buy.oyc.commons.domain.AuditLogType
import org.jesperancinha.concert.buy.oyc.commons.domain.BuyOycType
import org.jesperancinha.concert.buy.oyc.commons.domain.TicketReservation
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

/**
 * Data converters
 */
val TicketDto.toTicketData: TicketReservation
    get() = TicketReservation(
        name = name,
        reference = reference,
        address = address,
        birthDate = birthDate,
    )

data class TicketDto(
    val name: String,
    val reference: UUID? = null,
    val address: String,
    val birthDate: LocalDate,
    val concertDays: List<ConcertDayDto> = emptyList(),
    val meals: List<MealDto> = emptyList(),
    val drinks: List<DrinkDto> = emptyList(),
    val parkingReservation: ParkingReservationDto? = null,
    val createdAt: LocalDateTime? = LocalDateTime.now(),
    override val type: AuditLogType = AuditLogType.TICKET,
) : Serializable, BuyOycType

val TicketReservation.toDto: TicketDto
    get() = TicketDto(
        name = name,
        reference = reference,
        address = address,
        birthDate = birthDate,
        createdAt = createdAt
    )

