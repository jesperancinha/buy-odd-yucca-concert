package org.jesperaninha.concert.buy.oyc.containers

import io.micronaut.context.DefaultApplicationContextBuilder
import org.jesperaninha.concert.buy.oyc.containers.AbstractContainersTest.Companion.dockerCompose
import org.junit.jupiter.api.AfterAll
import org.slf4j.LoggerFactory
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.Wait.defaultWaitStrategy
import java.io.File
import java.time.Duration.ofMinutes


class DockerCompose(files: List<File>) : DockerComposeContainer<DockerCompose>(files)

/**
 * Created by jofisaes on 22/04/2022
 * Reminder: Don't use container_name with testcontainers. It just doesn't work
 */
abstract class AbstractContainersTest {
    companion object {
        private val file1 = File("../docker-compose-it.yml")
        private val file2 = File("docker-compose-it.yml")
        private val finalFile = if (file1.exists()) file1 else file2

        @JvmStatic
        val dockerCompose: DockerCompose = DockerCompose(listOf(finalFile))
            .withExposedService("yucca-db", 5432, defaultWaitStrategy().withStartupTimeout(ofMinutes(5)))
            .withExposedService("redis_1", 6379, defaultWaitStrategy().withStartupTimeout(ofMinutes(5)))
            .withExposedService("kong_1", 8000, defaultWaitStrategy().withStartupTimeout(ofMinutes(5)))
            .withExposedService("buy-oyc-ticket_1", 8084, defaultWaitStrategy().withStartupTimeout(ofMinutes(5)))
            .withExposedService("buy-oyc-concert_1", 8085, defaultWaitStrategy().withStartupTimeout(ofMinutes(5)))
            .withExposedService("buy-oyc-parking_1", 8086, defaultWaitStrategy().withStartupTimeout(ofMinutes(5)))
            .withExposedService("buy-oyc-catering_1", 8087, defaultWaitStrategy().withStartupTimeout(ofMinutes(5)))
            .withExposedService("buy-oyc-api_1", 8088, defaultWaitStrategy().withStartupTimeout(ofMinutes(5)))
            .withExposedService("buy-oyc-nginx_1", 8080, defaultWaitStrategy().withStartupTimeout(ofMinutes(5)))
            .withLocalCompose(true)

        @JvmStatic
        @AfterAll
        fun tearDown() {
            dockerCompose.stop()
        }
    }
}

class CustomContextBuilder : DefaultApplicationContextBuilder() {
    init {
        eagerInitSingletons(true)
        dockerCompose
            .also {
                it.start()
            }
            .also {
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
