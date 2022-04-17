package org.jesperancinha.concert.buy.oyc.catering.rest

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import org.jesperancinha.concert.buy.oyc.commons.domain.MealReservationRepository

@Controller("/")
class WelcomeController(
    private val mealReservationRepository: MealReservationRepository,
) {
    @Get(value = "/", produces = [MediaType.APPLICATION_JSON])
    fun getWelcomeMessage() = "message" to "Welcome to the Catering service"
}