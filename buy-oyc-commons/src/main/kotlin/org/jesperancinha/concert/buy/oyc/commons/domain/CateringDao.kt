package org.jesperancinha.concert.buy.oyc.commons.domain

import io.micronaut.data.annotation.*
import io.micronaut.data.model.naming.NamingStrategies.UnderScoreSeparatedLowerCase
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

@MappedEntity(value = "drink", namingStrategy = UnderScoreSeparatedLowerCase::class)
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
    @field:DateCreated
    val createdAt: LocalDateTime? = LocalDateTime.now(),
)

/**
 * Created by jofisaes on 18/12/2021
 */
@MappedEntity(value = "meal", namingStrategy = UnderScoreSeparatedLowerCase::class)
data class Meal(
    @field: Id
    @field: AutoPopulated
    val id: UUID? = null,
    val coupon: UUID? = null,
    val boxType: BoxType,
    val discount: Long,
    val price: BigDecimal,
    val processed: Boolean = false,
    @field:DateCreated
    val createdAt: LocalDateTime? = LocalDateTime.now(),
)

@MappedEntity(value = "drink_reservation", namingStrategy = UnderScoreSeparatedLowerCase::class)
data class DrinkReservation(
    @field: Id
    @field: AutoPopulated
    val id: UUID? = null,
    val reference: UUID? = UUID.randomUUID(),
    val ticketReservation: TicketReservation,
    val drink: Drink
)

@MappedEntity(value = "meal_reservation", namingStrategy = UnderScoreSeparatedLowerCase::class)
data class MealReservation(
    @field: Id
    @field: AutoPopulated
    val id: UUID? = null,
    val reference: UUID? = UUID.randomUUID(),
    val ticketReservation: TicketReservation,
    val meal: Meal
)

@R2dbcRepository(dialect = Dialect.POSTGRES)
interface DrinkRepository : CoroutineCrudRepository<Drink, UUID>,
    CoroutineJpaSpecificationExecutor<DrinkReservation>

@R2dbcRepository(dialect = Dialect.POSTGRES)
interface MealRepository : CoroutineCrudRepository<Meal, UUID>,
    CoroutineJpaSpecificationExecutor<MealReservation>

@R2dbcRepository(dialect = Dialect.POSTGRES)
interface DrinkReservationRepository : CoroutineCrudRepository<DrinkReservation, UUID>,
    CoroutineJpaSpecificationExecutor<DrinkReservation> {
    @Join(value = "ticketReservation", type = Join.Type.FETCH)
    override suspend fun findById(id: UUID): DrinkReservation
}

@R2dbcRepository(dialect = Dialect.POSTGRES)
interface MealReservationRepository : CoroutineCrudRepository<MealReservation, UUID>,
    CoroutineJpaSpecificationExecutor<MealReservation> {

    @Join(value = "ticketReservation", type = Join.Type.FETCH)
    override suspend fun findById(id: UUID): MealReservation
}