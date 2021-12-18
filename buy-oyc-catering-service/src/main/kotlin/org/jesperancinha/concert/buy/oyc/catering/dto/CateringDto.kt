package org.jesperancinha.concert.buy.oyc.catering.dto

import org.jesperancinha.concert.buy.oyc.commons.domain.BoxType
import org.jesperancinha.concert.buy.oyc.commons.domain.Meal
import java.math.BigDecimal
import java.util.*


data class MealDto(
    val mN: UUID,
)

val Meal.toDto
    get() = MealDto(mN = id)

val MealDto.toData: Meal
    get() = Meal(
        id = mN,
        boxType = BoxType.L,
        coupon = UUID.randomUUID(),
        discount = 5,
        price = BigDecimal.ONE,
        processed= true
    )
