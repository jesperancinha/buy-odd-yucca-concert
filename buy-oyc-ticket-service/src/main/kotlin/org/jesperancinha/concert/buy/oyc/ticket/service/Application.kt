package org.jesperancinha.concert.buy.oyc.ticket.service

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("buy.oyc.ticket.service")
		.start()
}

