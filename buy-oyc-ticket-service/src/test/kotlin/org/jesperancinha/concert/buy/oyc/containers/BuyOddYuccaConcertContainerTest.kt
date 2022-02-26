package org.jesperancinha.concert.buy.oyc.containers

import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container

class TestPostgresSQLContainer(imageName: String) : PostgreSQLContainer<TestPostgresSQLContainer>(imageName)

abstract class BuyOddYuccaConcertContainerTest {
    companion object {
        @Container
        @JvmField
        val postgreSQLContainer: TestPostgresSQLContainer = TestPostgresSQLContainer("postgres:12")
            .withUsername("kong")
            .withPassword("kong")
            .withDatabaseName("yucca")
            .withExtraHost("localhost", "127.0.0.1")
            .withExposedPorts(5432)

        init {
            postgreSQLContainer.start()
        }
    }

}