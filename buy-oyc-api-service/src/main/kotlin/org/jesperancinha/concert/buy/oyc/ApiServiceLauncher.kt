package org.jesperancinha.concert.buy.oyc

import io.micronaut.runtime.Micronaut.build
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License
import io.swagger.v3.oas.annotations.servers.Server

@OpenAPIDefinition(
    info = Info(
        title = "OpenAPI definition",
        version = "0.0.0-SNAPSHOT",
        description = "Buy Odd Yucca Concert API - Api Service - Gateway application (not Kong) to make online reservations",
        license = License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"),
    ),
    servers = [Server(url = "http://localhost:8000/api/yucca-api", description = "Server URL")]
)
object ApiServiceLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        build()
            .args(*args)
            .packages("org.jesperancinha.concert.buy.oyc")
            .start()
    }
}

