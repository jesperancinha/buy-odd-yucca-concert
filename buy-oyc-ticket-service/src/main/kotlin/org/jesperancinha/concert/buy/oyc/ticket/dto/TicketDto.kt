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
    val meals: List<Meal> = emptyList(),
    val drinks: List<Drink> = emptyList(),
    val parkingReservation: ParkingReservation? = null,
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
    val coupon: UUID = UUID.randomUUID(),
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

val TicketReservation.toDto: TicketDto
    get() = TicketDto(
        name = name,
        address = address,
        birthDate = birthDate
    )
