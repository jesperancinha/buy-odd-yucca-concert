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
) : Serializable, BuyOycType

data class ConcertDayDto(
    val name: String,
    val description: String,
    val date: LocalDate,
    val createdAt: LocalDateTime? = LocalDateTime.now()
)

data class MealDto(
    val coupon: UUID? = null,
    val boxType: BoxType,
    val discount: Long,
    val price: BigDecimal,
    val processed: Boolean = false,
    val createdAt: LocalDateTime? = LocalDateTime.now(),
)

val TicketDto.toMealDto: List<MealDto>
    get() = this.meals.map {
        MealDto(
            coupon = it.coupon,
            boxType = it.boxType,
            discount = it.discount,
            price = it.price,
            processed = it.processed,
            createdAt = it.createdAt
        )
    }

data class DrinkDto(
    val name: String,
    val width: Long,
    val height: Long,
    val shape: String,
    val volume: Long,
    val price: BigDecimal,
)

val TicketDto.toDrinkDto: List<DrinkDto>
    get() = this.drinks.map {
        DrinkDto(
            name = it.name,
            width = it.width,
            height = it.height,
            shape = it.shape,
            volume = it.volume,
            price = it.price
        )
    }

data class ParkingReservationDto(
    val reference: UUID = UUID.randomUUID(),
    var carParkingId: Long,
    val createdAt: LocalDateTime? = LocalDateTime.now()
)


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

val TicketDto.toConcertData: List<ConcertDay>
    get() = this.concertDays.map {
        ConcertDay(
            name = it.name,
            description = it.description,
            concert_date = it.date,
            createdAt = it.createdAt
        )
    }

val TicketDto.toParkingData: ParkingReservation?
    get() = parkingReservation?.let {
        ParkingReservation(
            reference = it.reference
        )
    }
