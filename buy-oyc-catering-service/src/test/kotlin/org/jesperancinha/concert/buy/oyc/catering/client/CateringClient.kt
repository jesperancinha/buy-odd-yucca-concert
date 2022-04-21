package org.jesperancinha.concert.buy.oyc.catering.client

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client
import io.reactivex.rxjava3.core.Single
import org.jesperancinha.concert.buy.oyc.commons.dto.DrinkDto
import org.jesperancinha.concert.buy.oyc.commons.dto.MealDto
import org.jesperancinha.concert.buy.oyc.commons.dto.ParkingReservationDto
import reactor.core.publisher.Flux


/**
 * Created by jofisaes on 21/04/2022
 */
@Client("/api")
interface CateringReactiveClient {
    @Post("meal")
    fun createMeal(@Body parkingReservationDto: MealDto): Single<LinkedHashMap<String, String>>

    @Post("drink")
    fun createDrink(@Body parkingReservationDto: DrinkDto): Single<LinkedHashMap<String, String>>
}