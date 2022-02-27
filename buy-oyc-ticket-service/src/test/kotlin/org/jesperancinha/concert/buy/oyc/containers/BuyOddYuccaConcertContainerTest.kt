package org.jesperancinha.concert.buy.oyc.containers

import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.HostConfig
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container

class TestPostgresSQLContainer(imageName: String) : PostgreSQLContainer<TestPostgresSQLContainer>(imageName)

abstract class BuyOddYuccaConcertContainerTest(
) {
    companion object {
        @Container
        @JvmField
        val postgreSQLContainer: TestPostgresSQLContainer = TestPostgresSQLContainer("postgres:14")
            .withUsername("kong")
            .withPassword("kong")
            .withDatabaseName("yucca")
            .withExposedPorts(5432)
            .withCreateContainerCmdModifier { cmd ->
                cmd.withHostConfig(
                    HostConfig().withPortBindings(PortBinding(Ports.Binding.bindPort(5432), ExposedPort(5432)))
                )
            }

        init {
            postgreSQLContainer.start()

        }
    }

}