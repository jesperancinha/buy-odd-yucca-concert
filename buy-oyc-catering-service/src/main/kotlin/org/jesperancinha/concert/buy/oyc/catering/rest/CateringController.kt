package org.jesperancinha.concert.buy.oyc.catering.rest

import io.micronaut.http.HttpResponse.status
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import org.jesperancinha.concert.buy.oyc.catering.dto.MealDto
import org.jesperancinha.concert.buy.oyc.commons.domain.MealReservationRepository
import javax.validation.Valid

@Controller("/api")
class CateringController(
    private val mealReservationRepository: MealReservationRepository
) {
    @Post
    suspend fun saveParkingReservation(@Body mealDto: @Valid MealDto?): MutableHttpResponse<Pair<Int, String>> =
        mealDto?.let {
//            mealRepository.save(mealDto.toData)
            status<Map<Int, String>>(HttpStatus.CREATED).body(HttpStatus.CREATED.code to "Saved successfully !")
        } ?: status(HttpStatus.NOT_FOUND)
}