package org.jesperancinha.concert.buy.oyc.ticket.service

import jakarta.inject.Singleton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.jesperancinha.concert.buy.oyc.commons.domain.TicketRepository
import org.jesperancinha.concert.buy.oyc.commons.domain.TicketReservation
import org.jesperancinha.concert.buy.oyc.commons.dto.TicketDto
import org.jesperancinha.concert.buy.oyc.commons.dto.toDto
import org.jesperancinha.concert.buy.oyc.commons.dto.toTicketData
import javax.validation.Valid

/**
 * Created by jofisaes on 30/03/2022
 */
@Singleton
class TicketService(
    private val ticketRepository: TicketRepository,
) {
    suspend fun createTicket(ticketDto: @Valid TicketDto) = GlobalScope.launch {
        val ticketReservation = ticketDto.toTicketData
        ticketRepository.save(ticketReservation).toDto
    }

    fun getAll(): Flow<TicketReservation> = ticketRepository.findAll()
}