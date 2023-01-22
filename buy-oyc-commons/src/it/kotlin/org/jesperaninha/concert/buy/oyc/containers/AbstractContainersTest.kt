package org.jesperaninha.concert.buy.oyc.containers

import io.micronaut.context.DefaultApplicationContextBuilder
import org.jesperaninha.concert.buy.oyc.containers.AbstractContainersTest.Companion.dockerCompose
import org.junit.jupiter.api.AfterAll
import org.slf4j.LoggerFactory
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait.defaultWaitStrategy
import java.io.File
import java.time.Duration.ofMinutes

class DockerCompose(files: List<File>) : DockerComposeContainer<DockerCompose>(files)

private val logger = LoggerFactory.getLogger(AbstractContainersTest::class.java)
private val logConsumer: Slf4jLogConsumer = Slf4jLogConsumer(logger)

/**
 * Created by jofisaes on 22/04/2022
 * Reminder: Don't use container_name with testcontainers. It just doesn't work
 */
abstract class AbstractContainersTest {
    companion object {
        private val file1 = File("../docker-compose-it.yml")
        private val file2 = File("docker-compose-it.yml")
        private val finalFile = if (file1.exists()) file1 else file2


        val dockerCompose: DockerCompose by lazy {
            DockerCompose(listOf(finalFile))
                .withBuyOycContainer("yucca-db", 5432)
                .withBuyOycContainer("redis_1", 6379)
                .withBuyOycContainer("kong_1", 8001)
                .withBuyOycContainer("kong_1", 8000)
                .withBuyOycContainer("buy-oyc-ticket_1", 8084)
                .withBuyOycContainer("buy-oyc-concert_1", 8085)
                .withBuyOycContainer("buy-oyc-parking_1", 8086)
                .withBuyOycContainer("buy-oyc-catering_1", 8087)
                .withBuyOycContainer("buy-oyc-api_1", 8088)
                .withBuyOycContainer("buy-oyc-nginx_1", 8080)
                .withLocalCompose(true)
        }

        @JvmStatic
        @AfterAll
        fun tearDown() {
            dockerCompose.stop()
        }

    }
}

private fun DockerCompose.withBuyOycContainer(serviceName: String, port: Int): DockerCompose =
    withExposedService(serviceName, port, defaultWaitStrategy().withStartupTimeout(ofMinutes(5)))
        .withLogConsumer(serviceName, logConsumer)

class CustomContextBuilder : DefaultApplicationContextBuilder() {
    init {
        eagerInitSingletons(true)

        dockerCompose
            .also {
                logger.info("Starting docker compose...")
                runCatching {
                    it.start()
                }.getOrElse {
                    logger.error("An error has occurred!", it)
                    logger.error(it.stackTraceToString(), it)
                }
                logger.info("Docker compose has started!")
            }
            .also {
                logger.info("Configuring properties...")
                val serviceHost = it.getServiceHost("yucca-db", 5432)
                val servicePort = it.getServicePort("yucca-db", 5432)
                logger.info("Preconfigured service host is $serviceHost")
                logger.info("Preconfigured service port is $servicePort")
            }
        dockerCompose.waitingFor("yucca-db", defaultWaitStrategy())
        val containerByServiceName = dockerCompose.getContainerByServiceName("yucca-db_1")
        val containerState = containerByServiceName.get()
        val servicePort = containerState.firstMappedPort
        val serviceHost = containerState.host
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
