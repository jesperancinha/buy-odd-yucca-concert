package org.jesperancinha.concert.buy.oyc.commons.domain

import io.micronaut.data.annotation.*
import io.micronaut.data.annotation.Relation.Kind.MANY_TO_ONE
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
    val reference: UUID = UUID.randomUUID(),
    val name: String,
    val width: Long,
    val height: Long,
    val shape: String,
    val volume: Long,
    val price: BigDecimal,
    val ticketReservation: TicketReservation,
    @field:DateCreated
    val createdAt: LocalDateTime? = LocalDateTime.now(),
)

/**
 * Created by jofisaes on 18/12/2021
 */
@MappedEntity(value = "meal", namingStrategy = NamingStrategies.UnderScoreSeparatedLowerCase::class)
data class Meal(
    @field: Id
    @field: AutoPopulated
    val id: UUID? = null,
    val reference: UUID = UUID.randomUUID(),
    val coupon: UUID? = null,
    val boxType: BoxType,
    val discount: Long,
    val price: BigDecimal,
    val processed: Boolean = false,
    val ticketReservation: TicketReservation,
    @field:DateCreated
    val createdAt: LocalDateTime? = LocalDateTime.now(),
)

@R2dbcRepository(dialect = Dialect.POSTGRES)
interface DrinkRepository : CoroutineCrudRepository<Drink, UUID>,
    CoroutineJpaSpecificationExecutor<Drink> {
    @Join(value = "ticketReservation", type = Join.Type.FETCH)
    override suspend fun findById(id: UUID): Drink
}

@R2dbcRepository(dialect = Dialect.POSTGRES)
interface MealRepository : CoroutineCrudRepository<Meal, UUID>,
    CoroutineJpaSpecificationExecutor<Meal> {

    @Join(value = "ticketReservation", type = Join.Type.FETCH)
    override suspend fun findById(id: UUID): Meal
}