package org.jesperancinha.concert.buy.oyc.ticket.service

import io.kotest.matchers.booleans.shouldBeTrue
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.jesperancinha.concert.buy.oyc.containers.AbstractBuyOddYuccaConcertContainerTest
import org.junit.jupiter.api.Test

@MicronautTest(transactional = false)
class BuyOycTicketServiceTest(
    @Inject
    val application: EmbeddedApplication<*>
) : AbstractBuyOddYuccaConcertContainerTest(){

    @Test
    fun testItWorks() {
        application.isRunning.shouldBeTrue()
    }

}
