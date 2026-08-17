package org.jesperancinha.concert.buy.oyc.containers

import io.micronaut.test.support.TestPropertyProvider
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.ClassicConfiguration
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.postgresql.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName.parse

class TestPostgresSQLContainer(imageName: String) : PostgreSQLContainer(imageName)

private const val POSTGRESQL_PORT = 5432
private const val REDIS_PORT = 6379

abstract class AbstractBuyOddYuccaConcertContainerTest : TestPropertyProvider {
    companion object {
        val postgreSQLContainer: PostgreSQLContainer = TestPostgresSQLContainer("postgres:16-alpine")
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
            setRedis()
            setPostgreSQL()
        }

        fun setPostgreSQL() {
            val postgresPort = postgreSQLContainer.getMappedPort(POSTGRESQL_PORT)
            System.setProperty(
                "r2dbc.datasources.default.url",
                "r2dbc:postgresql://kong:kong@${postgreSQLContainer.host}:$postgresPort/yucca?currentSchema=ticket"
            )
            System.setProperty(
                "POSTGRESQL_HOST",
                postgreSQLContainer.host
            )
            System.setProperty(
                "POSTGRESQL_PORT",
                postgresPort.toString()
            )
        }

        fun setRedis() {
            val redisPort = redis.getMappedPort(REDIS_PORT)
            System.setProperty(
                "redis.uri",
                "redis://${redis.host}:$redisPort"
            )
            System.setProperty(
                "REDIS_HOST",
                redis.host
            )
            System.setProperty(
                "REDIS_PORT",
                redisPort.toString()
            )
            System.setProperty(
                "redis.host",
                redis.host
            )
            System.setProperty(
                "redis.port",
                redisPort.toString()
            )
        }
    }

    override fun getProperties(): MutableMap<String, String> {
        val redisPort = redis.getMappedPort(REDIS_PORT)
        val postgresqlPort = postgreSQLContainer.getMappedPort(POSTGRESQL_PORT)
        return mutableMapOf(
            "r2dbc.datasources.default.url" to "r2dbc:postgresql://kong:kong@${postgreSQLContainer.host}:$postgresqlPort/yucca?currentSchema=ticket",
            "redis.uri" to "redis://${redis.host}:$redisPort",
            "redis.host" to redis.host,
            "redis.port" to redisPort.toString(),
            "REDIS_HOST" to redis.host,
            "REDIS_PORT" to redisPort.toString(),
            "POSTGRESQL_HOST" to postgreSQLContainer.host,
            "POSTGRESQL_PORT" to postgresqlPort.toString()
        )
    }
}