package org.jesperaninha.concert.buy.oyc.containers

import io.micronaut.context.DefaultApplicationContextBuilder
import org.jesperaninha.concert.buy.oyc.containers.AbstractContainersTest.Companion.dockerCompose
import org.junit.jupiter.api.AfterAll
import org.slf4j.LoggerFactory
import org.testcontainers.containers.ComposeContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait.defaultWaitStrategy
import java.io.File
import java.time.Duration.ofMinutes

class DockerCompose(files: List<File>) : ComposeContainer(files)

private val logger = LoggerFactory.getLogger(AbstractContainersTest::class.java)
private val logConsumer: Slf4jLogConsumer = Slf4jLogConsumer(logger)

private const val YUCCA_DB_SERVICE_NAME = "yucca-db"
private const val YUCCA_DB_SERVICE_PORT = 5432

/**
 * Reminder: Don't use container_name with testcontainers. It just doesn't work
 */
abstract class AbstractContainersTest {
    companion object {
        private val file1 = File("../docker-compose-it.yml")
        private val file2 = File("docker-compose-it.yml")
        private val envFile1 = File("../.env")
        private val envFile2 = File(".env")
        private val finalFile = if (file1.exists()) file1 else file2
        private val finalEnvFile = if (envFile1.exists()) envFile1 else envFile2

        val dockerCompose: DockerCompose by lazy {
            DockerCompose(listOf(finalFile))
                .withBuyOycContainer(YUCCA_DB_SERVICE_NAME, YUCCA_DB_SERVICE_PORT)
                .withBuyOycContainer("redis", 6379)
                .withBuyOycContainer("kong", 8000)
                .withBuyOycContainer("kong", 8001)
                .withBuyOycContainer("buy-oyc-ticket", 8084)
                .withBuyOycContainer("buy-oyc-concert", 8085)
                .withBuyOycContainer("buy-oyc-parking", 8086)
                .withBuyOycContainer("buy-oyc-catering", 8087)
                .withBuyOycContainer("buy-oyc-api", 8088)
                .withBuyOycContainer("buy-oyc-nginx", 8080)
                .withEnv(finalEnvFile.readText().split("\n").mapNotNull {
                    it.takeIf { it.contains("=") }?.let {
                        val (key, value) = it.split("=")
                        key to value
                    }
                }.toMap()) as DockerCompose
        }

        @JvmStatic
        @AfterAll
        fun tearDown() {
            dockerCompose.stop()
        }

    }
}

private const val STARTUP_CONTAINER_TIMEOUT_MINUTES = 10L

private fun DockerCompose.withBuyOycContainer(serviceName: String, port: Int): DockerCompose =
    withExposedService(
        serviceName,
        port,
        defaultWaitStrategy().withStartupTimeout(ofMinutes(STARTUP_CONTAINER_TIMEOUT_MINUTES))
    )
        .withLogConsumer(serviceName, logConsumer) as DockerCompose

class CustomContextBuilder : DefaultApplicationContextBuilder() {
    init {
        eagerInitSingletons(true)

        dockerCompose
            .also { compose ->
                logger.info("Starting docker compose...")
                runCatching {
                    compose.start()
                }.getOrElse {
                    logger.error("An error has occurred!", it)
                    logger.error(it.stackTraceToString(), it)
                }
                logger.info("Docker compose has started!")
            }.waitingFor(YUCCA_DB_SERVICE_NAME, defaultWaitStrategy())
        val servicePort = dockerCompose.getServicePort(YUCCA_DB_SERVICE_NAME, YUCCA_DB_SERVICE_PORT)
        val serviceHost = dockerCompose.getServiceHost(YUCCA_DB_SERVICE_NAME, YUCCA_DB_SERVICE_PORT)
        logger.info("Configuring properties...")
        logger.info("Preconfigured service host is $serviceHost")
        logger.info("Preconfigured service port is $servicePort")
        val props = mapOf(
            "r2dbc.datasources.default.url" to "r2dbc:postgresql://kong@$serviceHost:$servicePort/yucca?currentSchema=ticket"
        )
        logger.info("Database Host configuration is $props")
        properties(props)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(CustomContextBuilder::class.java)
    }
}
