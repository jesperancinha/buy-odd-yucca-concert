package org.jesperancinha.concert.buy.oyc.ticket

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/")
class WelcomeController {
    @Get(value = "/", produces = [MediaType.APPLICATION_JSON])
    fun getAllParkingReservations() = "message" to "Welcome to the Ticket service"
}