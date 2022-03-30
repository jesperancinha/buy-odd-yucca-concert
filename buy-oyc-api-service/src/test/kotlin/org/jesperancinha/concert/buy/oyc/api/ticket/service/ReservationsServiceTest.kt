package org.jesperancinha.concert.buy.oyc.api.ticket.service

import io.kotest.matchers.nulls.shouldNotBeNull
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.ClassicConfiguration
import org.jesperancinha.concert.buy.oyc.api.containers.AbstractBuyOddYuccaConcertContainerTest
import org.jesperancinha.concert.buy.oyc.api.service.ReservationsService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


@MicronautTest
internal class ReservationsServiceTest @Inject constructor(
    private val reservationsService: ReservationsService
) : AbstractBuyOddYuccaConcertContainerTest() {

    private val config = ClassicConfiguration()

    @BeforeEach
    fun beforeEach() {
        postgreSQLContainer.start()
        config.setDataSource(
            postgreSQLContainer.jdbcUrl,
            postgreSQLContainer.username,
            postgreSQLContainer.password
        )
        config.schemas = arrayOf("ticket")
        Flyway(config).migrate()
    }

    @Test
    fun `should get all reservations`(): Unit = runBlocking {
        reservationsService.getAll().toList().shouldNotBeNull()
    }
}

