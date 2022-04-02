package org.jesperancinha.concert.buy.oyc.api.rest

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.jesperancinha.concert.buy.oyc.api.dto.ReceiptDto
import org.jesperancinha.concert.buy.oyc.commons.domain.TicketRepository
import org.jesperancinha.concert.buy.oyc.commons.domain.TicketReservation
import org.jesperancinha.concert.buy.oyc.api.dto.TicketDto
import org.jesperancinha.concert.buy.oyc.api.dto.toDto
import org.jesperancinha.concert.buy.oyc.commons.domain.ReceiptRepository

@Controller("/")
open class WelcomeController(
    private val receiptRepository: ReceiptRepository
) {
    @Get(value = "/", produces = [MediaType.APPLICATION_JSON])
    open suspend fun getWelcomeMessage() = "message" to "Welcome to the Receipt service"

    @Get(value = "/test", produces = [MediaType.APPLICATION_JSON])
    open fun getAllParkingReservations(): Flow<ReceiptDto> {
        return receiptRepository.findAll().map { it.toDto }
    }
}