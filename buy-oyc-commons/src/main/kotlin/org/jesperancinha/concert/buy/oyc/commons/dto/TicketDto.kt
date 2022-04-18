package org.jesperancinha.concert.buy.oyc.commons.dto

import org.jesperancinha.concert.buy.oyc.commons.domain.*
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

/**
 * Data converters
 */
val TicketDto.toTicketData: TicketReservation
    get() = TicketReservation(
        name = name,
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

data class ConcertDayDto(
    val name: String,
    var reference: UUID? = null,
    val description: String,
    val concertDate: LocalDate,
    val createdAt: LocalDateTime? = LocalDateTime.now(),
    override val type: AuditLogType = AuditLogType.CONCERT_DAY
) : Serializable, BuyOycType
val TicketReservation.toDto: TicketDto
    get() = TicketDto(
        name = name,
        address = address,
        birthDate = birthDate
    )

val TicketDto.toParkingDto: ParkingReservationDto?
    get() = parkingReservation?.let {
        ParkingReservationDto(
            reference = it.reference,
            carParkingId = it.carParkingId,
            createdAt = it.createdAt
        )
    }
