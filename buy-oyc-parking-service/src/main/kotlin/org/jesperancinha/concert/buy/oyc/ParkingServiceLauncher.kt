package org.jesperancinha.concert.buy.oyc

import io.micronaut.runtime.Micronaut

object ParkingServiceLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
            .args(*args)
            .packages("org.jesperancinha.concert.buy.oyc")
            .start()
    }
}