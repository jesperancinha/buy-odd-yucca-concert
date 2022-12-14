package org.jesperancinha.concert.buy.oyc

import io.micronaut.runtime.Micronaut.*
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License
import io.swagger.v3.oas.annotations.servers.Server


@OpenAPIDefinition(
	info = Info(
		title = "OpenAPI definition",
		version = "0.0.0-SNAPSHOT",
		description = "Buy Odd Yucca Concert - Ticket Service - Background Service for the Aggregate Ticket management",
		license = License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"),
	),
	servers = [Server(url = "http://localhost:8000", description = "Server URL")]
)
object TicketServiceLauncher {
	@JvmStatic
	fun main(args: Array<String>) {
		build()
			.args(*args)
			.packages("org.jesperancinha.concert.buy.oyc")
			.start()
	}
}

