package org.jesperancinha.concert.buy.oyc

import io.micronaut.runtime.Micronaut.build

fun main(args: Array<String>) {
    build()
        .args(*args)
        .start()
}
