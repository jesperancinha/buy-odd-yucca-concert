package org.jesperancinha.concert.buy.oyc.ticket.service

import io.kotest.matchers.nulls.shouldNotBeNull
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.jesperancinha.concert.buy.oyc.containers.AbstractBuyOddYuccaConcertContainerTest
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
@MicronautTest(transactional = false)
internal class TicketServiceTest @Inject constructor(
    private val ticketService: TicketService
) : AbstractBuyOddYuccaConcertContainerTest() {

    @Test
    fun `should get all tickets`() = runTest {
        ticketService.getAll().toList().shouldNotBeNull()
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun setUp() {
            config.isCleanDisabled = false
            postgreSQLContainer.start()
            redis.start()
            config.setDataSource(
                postgreSQLContainer.jdbcUrl, postgreSQLContainer.username, postgreSQLContainer.password
            )
            config.schemas = arrayOf("ticket")
        }
    }
}

