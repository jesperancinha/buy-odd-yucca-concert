package org.jesperancinha.concert.buy.oyc.ticket.service

import io.kotest.matchers.booleans.shouldBeTrue
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.jesperancinha.concert.buy.oyc.containers.BuyOddYuccaConcertContainerTest
import org.junit.jupiter.api.Test

@MicronautTest
class BuyOycTicketServiceTest(
    @Inject
    val application: EmbeddedApplication<*>
) : BuyOddYuccaConcertContainerTest() {

    @Test
    fun testItWorks() {
        application.isRunning.shouldBeTrue()
    }

}
