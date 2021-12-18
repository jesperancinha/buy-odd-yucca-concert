package org.jesperancinha.concert.buy.oyc.catering.rest

import io.micronaut.http.HttpResponse.status
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.jesperancinha.concert.buy.oyc.catering.dto.MealDto
import org.jesperancinha.concert.buy.oyc.catering.dto.toData
import org.jesperancinha.concert.buy.oyc.catering.dto.toDto
import org.jesperancinha.concert.buy.oyc.commons.domain.MealRepository
import javax.validation.Valid

@Controller("/api")
class CateringController(
    private val mealRepository: MealRepository
) {
    @Post
    suspend fun saveParkingReservation(@Body mealDto: @Valid MealDto?): MutableHttpResponse<Pair<Int, String>> =
        mealDto?.let {
            mealRepository.save(mealDto.toData)
            status<Map<Int, String>>(HttpStatus.CREATED).body(HttpStatus.CREATED.code to "Saved successfully !")
        } ?: status(HttpStatus.NOT_FOUND)

    @Get(value = "/", produces = [MediaType.APPLICATION_JSON])
    fun getAllParkingReservations(): Flow<MealDto> {
        return mealRepository.findAll().map { it.toDto }
    }
}