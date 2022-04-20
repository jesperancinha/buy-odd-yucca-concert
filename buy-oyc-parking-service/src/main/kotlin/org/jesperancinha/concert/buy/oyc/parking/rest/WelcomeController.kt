package org.jesperancinha.concert.buy.oyc.parking.rest

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/")
open class WelcomeController {
    @Get(value = "/", produces = [MediaType.APPLICATION_JSON])
    open suspend fun getWelcomeMessage() = "message" to "Welcome to the Parking service"
}