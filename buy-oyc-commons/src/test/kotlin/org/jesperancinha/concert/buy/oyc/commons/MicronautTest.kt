package org.jesperancinha.concert.buy.oyc.commons

import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.jesperancinha.concert.buy.oyc.containers.AbstractBuyOddYuccaConcertContainerTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

@MicronautTest(transactional = false)
class MicronautTest(
    @Inject
    val application: EmbeddedApplication<*>
) : AbstractBuyOddYuccaConcertContainerTest() {

    @Test
    fun `should start a Micronaut application`() {
        Assertions.assertTrue(application.isRunning)
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun setUp() {
            postgreSQLContainer.start()
        }
    }

}
