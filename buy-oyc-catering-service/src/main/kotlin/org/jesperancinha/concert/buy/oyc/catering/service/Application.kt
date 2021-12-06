package org.jesperancinha.concert.buy.oyc.catering.service

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("buy.oyc.catering.service")
		.start()
}

