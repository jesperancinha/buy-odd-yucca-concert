package org.jesperancinha.concert.buy.oyc.containers

import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.HostConfig
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports.Binding.bindPort
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.ClassicConfiguration
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.utility.DockerImageName.parse

class TestPostgresSQLContainer(imageName: String) : PostgreSQLContainer<TestPostgresSQLContainer>(imageName)

private const val POSTGRESQL_PORT = 5432
private const val REDIS_PORT = 6379

abstract class AbstractBuyOddYuccaConcertContainerTest {
    companion object {
        @Container
        @JvmField
        val postgreSQLContainer: TestPostgresSQLContainer = TestPostgresSQLContainer("postgres:14")
            .withUsername("kong")
            .withPassword("kong")
            .withDatabaseName("yucca")
            .withExposedPorts(POSTGRESQL_PORT)
            .withCreateContainerCmdModifier { cmd ->
                cmd.withHostConfig(
                    HostConfig().withPortBindings(
                        PortBinding(
                            bindPort(POSTGRESQL_PORT),
                            ExposedPort(POSTGRESQL_PORT)
                        )
                    )
                )
            }


        @Container
        @JvmField
        val redis: GenericContainer<*> = GenericContainer(parse("redis:5.0.3-alpine"))
            .withExposedPorts(REDIS_PORT)
            .withCreateContainerCmdModifier { cmd ->
                cmd.withHostConfig(
                    HostConfig().withPortBindings(
                        PortBinding(
                            bindPort(REDIS_PORT),
                            ExposedPort(REDIS_PORT)
                        )
                    )
                )
            }

        val config = ClassicConfiguration()

        init {
            config.isCleanDisabled = false
            postgreSQLContainer.start()
            redis.start()
            config.setDataSource(
                postgreSQLContainer.jdbcUrl,
                postgreSQLContainer.username,
                postgreSQLContainer.password
            )
            config.schemas = arrayOf("ticket")
            Flyway(config).migrate()
        }
    }
}