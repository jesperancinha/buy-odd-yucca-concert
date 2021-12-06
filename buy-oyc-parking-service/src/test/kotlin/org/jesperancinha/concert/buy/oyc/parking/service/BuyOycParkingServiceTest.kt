package org.jesperancinha.concert.buy.oyc.parking.service
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import jakarta.inject.Inject

@MicronautTest
class BuyOycParkingServiceTest(
    @Inject
    val application: EmbeddedApplication<*>
) {

    @Test
    fun testItWorks() {
        Assertions.assertTrue(application.isRunning)
    }

}
