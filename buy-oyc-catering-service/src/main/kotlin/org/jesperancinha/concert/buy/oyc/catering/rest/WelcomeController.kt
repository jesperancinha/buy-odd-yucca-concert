package org.jesperancinha.concert.buy.oyc.catering.rest

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.jesperancinha.concert.buy.oyc.catering.dto.MealDto
import org.jesperancinha.concert.buy.oyc.catering.dto.toDto
import org.jesperancinha.concert.buy.oyc.commons.domain.Meal
import org.jesperancinha.concert.buy.oyc.commons.domain.MealRepository

@Controller("/")
class WelcomeController(
    private val mealRepository: MealRepository,
) {
    @Get(value = "/", produces = [MediaType.APPLICATION_JSON])
    fun getWelcomeMessage() = "message" to "Welcome to the Catering service"

    @Get(value = "/test", produces = [MediaType.APPLICATION_JSON])
    fun getAllCateringReservations(): Flow<MealDto> {
        return mealRepository.findAll().map { it.toDto }
    }
}