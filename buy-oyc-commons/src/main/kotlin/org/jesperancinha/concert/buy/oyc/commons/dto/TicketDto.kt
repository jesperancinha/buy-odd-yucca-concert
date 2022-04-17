package org.jesperancinha.concert.buy.oyc.commons.dto

import org.jesperancinha.concert.buy.oyc.commons.domain.*
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

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
    val description: String,
    val concertDate: LocalDate,
    val createdAt: LocalDateTime? = LocalDateTime.now(),
    override val type: AuditLogType = AuditLogType.CONCERT_DAY
) : Serializable, BuyOycType

data class ParkingReservationDto(
    val reference: UUID = UUID.randomUUID(),
    var carParkingId: Long,
    val createdAt: LocalDateTime? = LocalDateTime.now(),
    override val type: AuditLogType = AuditLogType.PARKING
): Serializable, BuyOycType


val ParkingReservation.toDto: ParkingReservationDto
    get() = ParkingReservationDto(
        reference = reference,
        carParkingId = carParking?.parkingNumber ?: -1,
        createdAt = createdAt
    )

val TicketReservation.toDto: TicketDto
    get() = TicketDto(
        name = name,
        address = address,
        birthDate = birthDate
    )

data class ReceiptDto(
    val reference: UUID,
    val createdAt: LocalDateTime,
)

val Receipt.toDto: ReceiptDto
    get() = ReceiptDto(
        reference = reference ?: throw RuntimeException("No reference found for this Receipt!"),
        createdAt = createdAt ?: throw RuntimeException("This Receipt does not have a created date!")
    )

/**
 * Data converters
 */

val TicketDto.toTicketData: TicketReservation
    get() = TicketReservation(
        name = name,
        address = address,
        birthDate = birthDate,
    )

val TicketDto.toConcertDto: List<ConcertDayDto>
    get() = this.concertDays.map {
        ConcertDayDto(
            name = it.name,
            description = it.description,
            concertDate = it.concertDate,
            createdAt = it.createdAt
        )
    }

val TicketDto.toParkingDto: ParkingReservationDto?
    get() = parkingReservation?.let {
        ParkingReservationDto(
            reference = it.reference,
            carParkingId = it.carParkingId,
            createdAt = it.createdAt
        )
    }
