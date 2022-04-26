package org.jesperancinha.concert.buy.oyc.commons.dto

import org.jesperancinha.concert.buy.oyc.commons.domain.*
import org.jesperancinha.concert.buy.oyc.commons.domain.AuditLogType.DRINK
import org.jesperancinha.concert.buy.oyc.commons.domain.AuditLogType.MEAL
import java.io.Serializable
import java.util.*

/**
 * Created by jofisaes on 17/04/2022
 */
data class MealDto(
    var reference: UUID? = null,
    val mealId: UUID?,
    val ticketReservationId: UUID?,
    override val type: AuditLogType = MEAL
) : Serializable, BuyOycType()

data class DrinkDto(
    var reference: UUID? = null,
    val drinkId: UUID?,
    val ticketReservationId: UUID?,
    override val type: AuditLogType = DRINK
) : Serializable, BuyOycType()

val DrinkReservation.toDto: DrinkDto
    get() = DrinkDto(
        reference = reference,
        drinkId = drink?.id,
        ticketReservationId = ticketReservation?.id
    )

fun DrinkDto.toData(ticketReservation: TicketReservation, drink: Drink) = DrinkReservation(
    reference = reference,
    drink = drink,
    ticketReservation = ticketReservation
)

val MealReservation.toDto: MealDto
    get() = MealDto(
        reference = reference,
        mealId = meal?.id,
        ticketReservationId = ticketReservation?.id
    )

fun MealDto.toData(ticketReservation: TicketReservation, meal: Meal) = MealReservation(
    reference = reference,
    meal = meal,
    ticketReservation = ticketReservation
)