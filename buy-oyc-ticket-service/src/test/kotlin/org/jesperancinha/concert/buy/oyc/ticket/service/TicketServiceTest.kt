package org.jesperancinha.concert.buy.oyc.ticket.service

import io.kotest.matchers.nulls.shouldNotBeNull
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.jesperancinha.concert.buy.oyc.containers.AbstractBuyOddYuccaConcertContainerTest
import org.junit.jupiter.api.Test


@MicronautTest
internal class TicketServiceTest @Inject constructor(
    private val ticketService: TicketService
) : AbstractBuyOddYuccaConcertContainerTest() {

    @Test
    fun `should get all ticket`(): Unit = runBlocking {
        ticketService.getAll().toList().shouldNotBeNull()
    }
}

