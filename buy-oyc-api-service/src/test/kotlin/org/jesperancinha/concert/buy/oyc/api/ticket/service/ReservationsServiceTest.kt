package org.jesperancinha.concert.buy.oyc.api.ticket.service

import io.kotest.matchers.nulls.shouldNotBeNull
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.jesperancinha.concert.buy.oyc.api.containers.AbstractBuyOddYuccaConcertContainerTest
import org.jesperancinha.concert.buy.oyc.api.dto.TicketDto
import org.jesperancinha.concert.buy.oyc.api.service.ReservationsService
import org.jesperancinha.concert.buy.oyc.commons.domain.Receipt
import org.jesperancinha.concert.buy.oyc.commons.domain.ReceiptRepository
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.LocalDate
import javax.transaction.Transactional


@Testcontainers
@MicronautTest
internal class ReservationsServiceTest @Inject constructor(
    private val reservationsService: ReservationsService,
    private val receiptRepository: ReceiptRepository,
) : AbstractBuyOddYuccaConcertContainerTest() {

    @Test
    @Transactional
    fun `should save receipt directly`() = runTest {
        val receipt = receiptRepository.save(Receipt())
        receipt.id.shouldNotBeNull()
    }

    @Test
    fun `should get all reservations`(): Unit = runBlocking {
        reservationsService.getAll().toList().shouldNotBeNull()
    }

    @Test
    @Transactional
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

