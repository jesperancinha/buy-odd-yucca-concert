package org.jesperancinha.concert.buy.oyc.commons

import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@MicronautTest
class MicronautTest(
    @Inject
    val application: EmbeddedApplication<*>
) {

    @Test
    fun `should start a Micronaut application`() {
        Assertions.assertTrue(application.isRunning)
    }

}
