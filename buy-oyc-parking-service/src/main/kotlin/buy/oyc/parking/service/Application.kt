package buy.oyc.parking.service

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("buy.oyc.parking.service")
		.start()
}

