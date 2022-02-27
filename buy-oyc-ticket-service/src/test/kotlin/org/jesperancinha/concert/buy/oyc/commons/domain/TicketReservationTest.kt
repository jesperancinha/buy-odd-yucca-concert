@file:OptIn(ExperimentalCoroutinesApi::class)

package org.jesperancinha.concert.buy.oyc.commons.domain

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.ClassicConfiguration
import org.jesperancinha.concert.buy.oyc.containers.AbstractBuyOddYuccaConcertContainerTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.LocalDate

/**
 * Created by jofisaes on 25/02/2022
 */
@Testcontainers
@MicronautTest
class TicketReservationTest @Inject constructor(
    private val ticketRepository: TicketRepository,
) : AbstractBuyOddYuccaConcertContainerTest() {

    private val config = ClassicConfiguration()

    @BeforeEach
    fun beforeEach() {
        postgreSQLContainer.start()
        config.setDataSource(postgreSQLContainer.jdbcUrl, postgreSQLContainer.username, postgreSQLContainer.password)
        config.schemas = arrayOf("ticket")
        Flyway(config).migrate()
    }

    @Test
    fun `should read an empty ticket list from repository`() = runTest {
        ticketRepository.findAll().toList().shouldBeEmpty()
    }

    @Test
    fun `should save simple ticket to repository`() = runTest {
        val birthDate = LocalDate.now()
        val ticketReservation = TicketReservation(
            name = "João",
            birthDate = birthDate,
            address = "Road to nowhere"
        )
        val (id, reference, name, address, birthDateResult, concertDays, meals, carParkingTicket, createdAt)
                = ticketRepository.save(ticketReservation)
        id.shouldNotBeNull()
        name shouldBe "João"
        reference.shouldNotBeNull()
        address shouldBe "Road to nowhere"
        birthDateResult shouldBeEqualComparingTo birthDate
        concertDays.shouldBeEmpty()
        meals.shouldBeEmpty()
        carParkingTicket.shouldBeNull()
        createdAt.shouldNotBeNull()
    }

    @AfterEach
    fun clean() {
        Flyway(config).clean()
    }
}