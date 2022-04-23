package org.jesperaninha.concert.buy.oyc.containers

import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import java.io.File

class DockerCompose(files: List<File>) : DockerComposeContainer<DockerCompose>(files)

/**
 * Created by jofisaes on 22/04/2022
 */
abstract class AbstractContainersTest {
    companion object {
        protected val dockerCompose: DockerCompose = DockerCompose(listOf(File("docker-compose-it.yml")))
            .withExposedService("buy-oyc-parking_1", 8085, Wait.defaultWaitStrategy())
            .withLocalCompose(true)
            .also {
                it.start()
            }
    }

}