package org.jesperancinha.concert.buy.oyc.containers

class ContainersContextBuilder : io.micronaut.context.DefaultApplicationContextBuilder() {
    init {
        eagerInitSingletons(true)
        val postgreSQLContainer = AbstractBuyOddYuccaConcertContainerTest.postgreSQLContainer
        val redis = AbstractBuyOddYuccaConcertContainerTest.redis
        properties(
            mapOf(
                "r2dbc.datasources.default.url" to "r2dbc:postgresql://${postgreSQLContainer.host}:${postgreSQLContainer.getMappedPort(5432)}/yucca?currentSchema=ticket",
                "redis.uri" to "redis://${redis.host}:${redis.getMappedPort(6379)}",
                "redis.host" to redis.host,
                "redis.port" to redis.getMappedPort(6379).toString(),
                "REDIS_HOST" to redis.host,
                "REDIS_PORT" to redis.getMappedPort(6379).toString(),
                "POSTGRESQL_HOST" to postgreSQLContainer.host,
                "POSTGRESQL_PORT" to postgreSQLContainer.getMappedPort(5432).toString()
            )
        )
    }
}
