package org.jesperancinha.concert.buy.oyc.commons.domain

import io.micronaut.data.annotation.AutoPopulated
import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.model.naming.NamingStrategies
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.r2dbc.annotation.R2dbcRepository
import io.micronaut.data.repository.jpa.kotlin.CoroutineJpaSpecificationExecutor
import io.micronaut.data.repository.kotlin.CoroutineCrudRepository
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

enum class BoxType {
    XS, S, M, L, XL
}

@MappedEntity(value = "drink", namingStrategy = NamingStrategies.UnderScoreSeparatedLowerCase::class)
data class Drink(
    @field: Id
    @field: AutoPopulated
    val id: UUID? = null,
    val name: String,
    val width: Long,
    val height: Long,
    val shape: String,
    val volume: Long,
    val price: BigDecimal,
)

/**
 * Created by jofisaes on 18/12/2021
 */
@MappedEntity(value = "meal", namingStrategy = NamingStrategies.UnderScoreSeparatedLowerCase::class)
data class Meal(
    @field: Id
    @field: AutoPopulated
    val id: UUID,
    val boxType: BoxType,
    val coupon: UUID,
    val discount: Long,
    val price: BigDecimal,
    val processed: Boolean,
    @field:DateCreated
    val createdAt: LocalDateTime? = LocalDateTime.now(),
)

@R2dbcRepository(dialect = Dialect.POSTGRES)
interface DrinkRepository : CoroutineCrudRepository<Drink, UUID>,
    CoroutineJpaSpecificationExecutor<Drink>

@R2dbcRepository(dialect = Dialect.POSTGRES)
interface MealRepository : CoroutineCrudRepository<Meal, UUID>,
    CoroutineJpaSpecificationExecutor<Meal>