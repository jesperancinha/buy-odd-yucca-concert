package org.jesperancinha.concert.buy.oyc

import io.micronaut.runtime.Micronaut.*

object ApiServiceLauncher {
	@JvmStatic
	fun main(args: Array<String>) {
		build()
			.args(*args)
			.packages("org.jesperancinha.concert.buy.oyc")
			.start()
	}
}

