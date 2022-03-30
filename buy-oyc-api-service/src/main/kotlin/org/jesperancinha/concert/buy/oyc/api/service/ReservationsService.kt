package org.jesperancinha.concert.buy.oyc.api.service

import jakarta.inject.Singleton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.jesperancinha.concert.buy.oyc.api.dto.TicketDto
import org.jesperancinha.concert.buy.oyc.api.dto.toDto
import org.jesperancinha.concert.buy.oyc.api.dto.toTicketData
import org.jesperancinha.concert.buy.oyc.commons.domain.*
import javax.validation.Valid

/**
 * Created by jofisaes on 30/03/2022
 */
@Singleton
class ReservationsService(
    private val reservationRepository: ReceiptRepository
) {
    suspend fun createTicket(ticketDto: @Valid TicketDto) = GlobalScope.launch {
        val ticketReservation = ticketDto.toTicketData

    }

    fun getAll(): Flow<Receipt> = reservationRepository.findAll()
}