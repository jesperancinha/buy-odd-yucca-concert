package org.jesperancinha.concert.buy.oyc.api.ticket.service

import io.kotest.matchers.nulls.shouldNotBeNull
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.jesperancinha.concert.buy.oyc.api.containers.AbstractBuyOddYuccaConcertContainerTest
import org.jesperancinha.concert.buy.oyc.api.dto.TicketDto
import org.jesperancinha.concert.buy.oyc.api.service.ReservationsService
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.time.LocalDate


@MicronautTest
internal class ReservationsServiceTest @Inject constructor(
    private val reservationsService: ReservationsService
) : AbstractBuyOddYuccaConcertContainerTest() {

    @Test
    fun `should get all reservations`(): Unit = runBlocking {
        reservationsService.getAll().toList().shouldNotBeNull()
    }

    @Test
    fun `should publish test`(): Unit = runBlocking {
        reservationsService.createTicket(
            TicketDto(
                name = "name",
                address = "address",
                birthDate = LocalDate.now()
            )
        )
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun setup() {
            redis.start()
            postgreSQLContainer.start()
        }
    }
}

