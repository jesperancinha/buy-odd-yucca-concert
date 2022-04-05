package org.jesperancinha.concert.buy.oyc.catering.dto

import org.jesperancinha.concert.buy.oyc.commons.domain.Meal
import java.util.*


data class MealDto(
    val mN: UUID?,
)

val Meal.toDto
    get() = MealDto(mN = id)
