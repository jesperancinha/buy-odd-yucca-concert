package org.jesperaninha.concert.buy.oyc.containers

import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import java.io.File

private class DockerCompose(files: List<File>) : DockerComposeContainer<DockerCompose>(files)

/**
 * Created by jofisaes on 22/04/2022
 */
abstract class AbstractContainersTest {
    companion object {
        init {
            DockerCompose(listOf(File("../docker-compose-it.yml")))
//                .withExposedService("kong", 8000, Wait.defaultWaitStrategy())
                .withLocalCompose(true)
                .also { it.start() }
        }
    }
}