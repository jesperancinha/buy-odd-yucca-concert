package org.jesperancinha.concert.buy.oyc.commons.domain

import io.micronaut.data.annotation.*
import io.micronaut.data.model.naming.NamingStrategies.UnderScoreSeparatedLowerCase
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.r2dbc.annotation.R2dbcRepository
import io.micronaut.data.repository.jpa.kotlin.CoroutineJpaSpecificationExecutor
import io.micronaut.data.repository.kotlin.CoroutineCrudRepository
import java.time.LocalDateTime
import java.util.*

@MappedEntity(value = "car_parking", namingStrategy = UnderScoreSeparatedLowerCase::class)
data class CarParking(
    @field: Id
    @field: AutoPopulated
    var id: UUID? = null,
    val parkingNumber: Long,
    @field:DateCreated
    val createdAt: LocalDateTime? = LocalDateTime.now()
)

@MappedEntity(namingStrategy = UnderScoreSeparatedLowerCase::class)
data class ParkingReservation(
    @Id
    @AutoPopulated
    var id: UUID? = null,
    val reference: UUID? = UUID.randomUUID(),
    var carParking: CarParking? = null,
    @DateCreated
    val createdAt: LocalDateTime? = LocalDateTime.now()
)

@R2dbcRepository(dialect = Dialect.POSTGRES)
interface ParkingReservationRepository : CoroutineCrudRepository<ParkingReservation, UUID>,
    CoroutineJpaSpecificationExecutor<ParkingReservation> {
    @Join(value = "carParking", type = Join.Type.FETCH)
    override suspend fun findById(id: UUID): ParkingReservation
}

@R2dbcRepository(dialect = Dialect.POSTGRES)
interface CarParkingRepository : CoroutineCrudRepository<CarParking, UUID>,
    CoroutineJpaSpecificationExecutor<CarParking> {
    fun findByParkingNumber(parkingNumber: Long): CarParking
}