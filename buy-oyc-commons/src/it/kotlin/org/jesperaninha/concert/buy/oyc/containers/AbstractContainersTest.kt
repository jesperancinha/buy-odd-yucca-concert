package org.jesperaninha.concert.buy.oyc.containers

import io.micronaut.context.DefaultApplicationContextBuilder
import org.jesperaninha.concert.buy.oyc.containers.AbstractContainersTest.Companion.dockerCompose
import org.slf4j.LoggerFactory
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.Wait.defaultWaitStrategy
import java.io.File


class DockerCompose(files: List<File>) : DockerComposeContainer<DockerCompose>(files)

/**
 * Created by jofisaes on 22/04/2022
 */
abstract class AbstractContainersTest {
    companion object {
        private val file1 = File("../docker-compose-it.yml")
        private val file2 = File("docker-compose-it.yml")
        private val finalFile = if (file1.exists()) file1 else file2
        private val logger = LoggerFactory.getLogger(AbstractContainersTest::class.java)

        @JvmStatic
        val dockerCompose: DockerCompose = DockerCompose(listOf(finalFile))
            .withExposedService("redis_1", 6379, defaultWaitStrategy())
            .withExposedService("kong_1", 8001, defaultWaitStrategy())
            .withExposedService("buy-oyc-ticket_1", 8084, defaultWaitStrategy())
            .withExposedService("buy-oyc-concert_1", 8085, defaultWaitStrategy())
            .withExposedService("buy-oyc-parking_1", 8086, defaultWaitStrategy())
            .withExposedService("buy-oyc-catering_1", 8087, defaultWaitStrategy())
            .withExposedService("buy-oyc-api_1", 8088, defaultWaitStrategy())
            .withLocalCompose(true)
            .also {
                it.start()
            }
            .also {
                val serviceHost = it.getServiceHost("db_1", 5432)
                logger.info("Preconfigured service host is $serviceHost")
            }
    }

}

class CustomContextBuilder : DefaultApplicationContextBuilder() {
    init {
        eagerInitSingletons(true)
        val serviceHost = dockerCompose.getServiceHost("db_1", 5432)
        val props = mapOf(
            "r2dbc.datasources.default.url" to "r2dbc:postgresql://kong@$serviceHost:5432/yucca?currentSchema=ticket"
        )
        logger.info("Database Host configuration is $props")
        properties(props)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(CustomContextBuilder::class.java)
    }
}