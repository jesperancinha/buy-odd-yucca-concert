package org.jesperancinha.concert.buy.oyc.ticket.dto

import org.jesperancinha.concert.buy.oyc.commons.domain.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class TicketDto(
    val name: String,
    val address: String,
    val birthDate: LocalDate,
    val concertDays: List<ConcertDayDto> = emptyList(),
    val meals: List<MealDto> = emptyList(),
    val drinks: List<DrinkDto> = emptyList(),
    val parkingReservation: ParkingReservationDto? = null,
    val createdAt: LocalDateTime? = LocalDateTime.now(),
)

val TicketDto.toTicketData: TicketReservation
    get() = TicketReservation(
        name = name,
        address = address,
        birthDate = birthDate,
    )

data class ConcertDayDto(
    val name: String,
    val description: String,
    val date: LocalDate,
    val createdAt: LocalDateTime? = LocalDateTime.now()
)

val TicketDto.toConcertData: List<ConcertDay>
    get() = this.concertDays.map {
        ConcertDay(
            name = it.name,
            description = it.description,
            date = it.date,
            createdAt = it.createdAt
        )
    }

data class MealDto(
    val coupon: UUID? = null,
    val boxType: BoxType,
    val discount: Long,
    val price: BigDecimal,
    val processed: Boolean = false,
    val createdAt: LocalDateTime? = LocalDateTime.now(),
)

val TicketDto.toMealData: List<Meal>
    get() = this.meals.map {
        Meal(
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

val TicketDto.toDrinkData: List<Drink>
    get() = this.drinks.map {
        Drink(
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

val ParkingReservationDto.toParkingReservationData: ParkingReservation
    get() = ParkingReservation(
        reference = reference
    )

val TicketReservation.toDto: TicketDto
    get() = TicketDto(
        name = name,
        address = address,
        birthDate = birthDate
    )

