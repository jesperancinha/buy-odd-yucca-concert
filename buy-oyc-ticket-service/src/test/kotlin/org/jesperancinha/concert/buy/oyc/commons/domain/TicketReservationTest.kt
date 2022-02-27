@file:OptIn(ExperimentalCoroutinesApi::class)

package org.jesperancinha.concert.buy.oyc.commons.domain

import io.kotest.matchers.collections.shouldBeEmpty
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.ClassicConfiguration
import org.jesperancinha.concert.buy.oyc.containers.BuyOddYuccaConcertContainerTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Testcontainers

/**
 * Created by jofisaes on 25/02/2022
 */
@Testcontainers
@MicronautTest(startApplication = false)
class TicketReservationTest @Inject constructor(
    private val ticketRepository: TicketRepository,
) : BuyOddYuccaConcertContainerTest() {

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

    @AfterEach
    fun clean() {
        Flyway(config).clean()
    }
}