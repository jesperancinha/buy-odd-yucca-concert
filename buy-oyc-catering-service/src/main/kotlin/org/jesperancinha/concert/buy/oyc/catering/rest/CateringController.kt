package org.jesperancinha.concert.buy.oyc.catering.rest

import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import jakarta.inject.Inject
import kotlinx.coroutines.DelicateCoroutinesApi
import org.jesperancinha.concert.buy.oyc.catering.service.CateringService
import org.jesperancinha.concert.buy.oyc.commons.dto.DrinkDto
import org.jesperancinha.concert.buy.oyc.commons.dto.MealDto
import org.jesperancinha.concert.buy.oyc.commons.dto.ResponseDto
import javax.validation.Valid

@Controller("api")
@DelicateCoroutinesApi
class CateringController @Inject constructor(
    private val cateringService: CateringService
) {
    @Post("meal")
    suspend fun createMeal(@Body mealDto: @Valid MealDto?): ResponseDto =
        mealDto?.let {
            cateringService.createMeal(mealDto)
            ResponseDto(code = HttpStatus.CREATED.code, message = "Saved successfully !")
        } ?: ResponseDto(code = HttpStatus.NOT_FOUND.code)

    @Post("drink")
    suspend fun createDrink(@Body drinkDto: @Valid DrinkDto?): ResponseDto =
        drinkDto?.let {
            cateringService.createDrink(drinkDto)
            ResponseDto(code = HttpStatus.CREATED.code, message = "Saved successfully !")
        } ?: ResponseDto(code = HttpStatus.NOT_FOUND.code)
}