package org.jesperancinha.concert.buy.oyc.api.service

import io.lettuce.core.api.StatefulRedisConnection
import jakarta.inject.Singleton
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.jesperancinha.concert.buy.oyc.api.dto.TicketDto
import org.jesperancinha.concert.buy.oyc.api.dto.toTicketData
import org.jesperancinha.concert.buy.oyc.commons.domain.Receipt
import org.jesperancinha.concert.buy.oyc.commons.domain.ReceiptRepository
import javax.validation.Valid

/**
 * Created by jofisaes on 30/03/2022
 */
@Singleton
class ReservationsService(
    private val reservationRepository: ReceiptRepository,
    private val connection: StatefulRedisConnection<String, String>
) {
    @OptIn(DelicateCoroutinesApi::class)
    fun createTicket(ticketDto: @Valid TicketDto) = GlobalScope.launch {
        val ticketReservation = ticketDto.toTicketData
    }

    fun getAll(): Flow<Receipt> = reservationRepository.findAll()
}