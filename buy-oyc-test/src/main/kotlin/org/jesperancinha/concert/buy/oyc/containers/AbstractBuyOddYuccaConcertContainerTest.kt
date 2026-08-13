package org.jesperancinha.concert.buy.oyc.containers

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.ClassicConfiguration
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.postgresql.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName.parse

class TestPostgresSQLContainer(imageName: String) : PostgreSQLContainer(imageName)

private const val POSTGRESQL_PORT = 5432
private const val REDIS_PORT = 6379

abstract class AbstractBuyOddYuccaConcertContainerTest {
    companion object {
        val postgreSQLContainer = TestPostgresSQLContainer("postgres:16-alpine")
            .withUsername("kong")
            .withPassword("kong")
            .withDatabaseName("yucca")
            .withExposedPorts(POSTGRESQL_PORT)
            .also { it.start() }


        val redis: GenericContainer<*> = GenericContainer(parse("redis:5.0.3-alpine"))
            .withExposedPorts(REDIS_PORT)
            .also { it.start() }

        val config = ClassicConfiguration()

        init {
            config.isCleanDisabled = false
            config.setDataSource(
                postgreSQLContainer.jdbcUrl,
                postgreSQLContainer.username,
                postgreSQLContainer.password
            )
            config.schemas = arrayOf("ticket")
            Flyway(config).migrate()
            postgreSQLContainer.waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*\\s", 1))
            redis.waitingFor(Wait.forLogMessage(".*Ready to accept connections.*\\s", 1))
            System.setProperty(
                "r2dbc.datasources.default.url",
                "r2dbc:postgresql://kong@${postgreSQLContainer.host}:${postgreSQLContainer.getMappedPort(POSTGRESQL_PORT)}/yucca?currentSchema=ticket"
            )
            System.setProperty(
                "redis.uri",
                "redis://${redis.host}:${redis.getMappedPort(REDIS_PORT)}"
            )
        }
    }
}