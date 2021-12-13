package org.jesperancinha.concert.buy.oyc.commons.domain

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.model.naming.NamingStrategies
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.r2dbc.annotation.R2dbcRepository
import io.micronaut.data.repository.CrudRepository

@MappedEntity(value = "parking_reservation", namingStrategy = NamingStrategies.UnderScoreSeparatedLowerCase::class)
data class ParkingReservation(
    @Id
    @GeneratedValue(value = GeneratedValue.Type.AUTO)
    val id: Long? = null,
    val parkingNumber: Long,
) {
    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , parkingNumber = $parkingNumber )"
    }
}

@R2dbcRepository(dialect = Dialect.POSTGRES)
interface ParkingReservationRepository : CrudRepository<ParkingReservation, Long>