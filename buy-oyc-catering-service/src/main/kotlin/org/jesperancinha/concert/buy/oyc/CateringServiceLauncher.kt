package org.jesperancinha.concert.buy.oyc

import io.micronaut.runtime.Micronaut.build

object CateringServiceLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        build()
            .args(*args)
            .packages("org.jesperancinha.concert.buy.oyc")
            .start()
    }
}