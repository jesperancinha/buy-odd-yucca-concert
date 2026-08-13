package org.jesperancinha.concert.buy.oyc.ticket.service

import io.kotest.matchers.nulls.shouldNotBeNull
import io.micronaut.context.DefaultApplicationContextBuilder
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.jesperancinha.concert.buy.oyc.containers.AbstractBuyOddYuccaConcertContainerTest
import org.junit.jupiter.api.Test

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
@MicronautTest(contextBuilder = [TicketServiceContextBuilder::class], transactional = false)
internal class TicketServiceTest : AbstractBuyOddYuccaConcertContainerTest() {

    @Inject
    lateinit var ticketService: TicketService

    @Test
    fun `should get all tickets`() = runTest {
        ticketService.getAll().toList().shouldNotBeNull()
    }
}

class TicketServiceContextBuilder : DefaultApplicationContextBuilder() {
    init {
        eagerInitSingletons(true)
        val postgreSQLContainer = AbstractBuyOddYuccaConcertContainerTest.postgreSQLContainer
        val redis = AbstractBuyOddYuccaConcertContainerTest.redis
        properties(
            mapOf(
                "r2dbc.datasources.default.url" to "r2dbc:postgresql://kong:kong@${postgreSQLContainer.host}:${postgreSQLContainer.getMappedPort(5432)}/yucca?currentSchema=ticket",
                "redis.uri" to "redis://${redis.host}:${redis.getMappedPort(6379)}",
                "redis.host" to redis.host,
                "redis.port" to redis.getMappedPort(6379).toString()
            )
        )
    }
}

