package org.jesperancinha.concert.buy.oyc.catering.rest

import io.micronaut.http.HttpResponse.status
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import jakarta.inject.Inject
import kotlinx.coroutines.DelicateCoroutinesApi
import org.jesperancinha.concert.buy.oyc.catering.service.CateringService
import org.jesperancinha.concert.buy.oyc.commons.dto.DrinkDto
import org.jesperancinha.concert.buy.oyc.commons.dto.MealDto
import javax.validation.Valid

@Controller("api")
@DelicateCoroutinesApi
class CateringController @Inject constructor(
    private val cateringService: CateringService
) {
    @Post("meal")
    suspend fun createMeal(@Body mealDto: @Valid MealDto?): MutableHttpResponse<Pair<Int, String>> =
        mealDto?.let {
            cateringService.createMeal(mealDto)
            status<Map<Int, String>>(HttpStatus.CREATED).body(HttpStatus.CREATED.code to "Saved successfully !")
        } ?: status(HttpStatus.NOT_FOUND)

    @Post("drink")
    suspend fun createDrink(@Body drinkDto: @Valid DrinkDto?): MutableHttpResponse<Pair<Int, String>> =
        drinkDto?.let {
            cateringService.createDrink(drinkDto)
            status<Map<Int, String>>(HttpStatus.CREATED).body(HttpStatus.CREATED.code to "Saved successfully !")
        } ?: status(HttpStatus.NOT_FOUND)
}