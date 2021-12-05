package buy.oyc.ticket.service

import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@MicronautTest
class BuyOycTicketServiceTest(
    @Inject
    val application: EmbeddedApplication<*>
) {

    @Test
    fun testItWorks() {
        Assertions.assertTrue(application.isRunning)
    }

}
