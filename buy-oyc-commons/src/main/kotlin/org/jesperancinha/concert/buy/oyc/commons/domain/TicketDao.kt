package org.jesperancinha.concert.buy.oyc.commons.domain

import io.micronaut.data.annotation.*
import io.micronaut.data.model.naming.NamingStrategies
import io.micronaut.data.model.query.builder.sql.Dialect.POSTGRES
import io.micronaut.data.r2dbc.annotation.R2dbcRepository
import io.micronaut.data.repository.jpa.kotlin.CoroutineJpaSpecificationExecutor
import io.micronaut.data.repository.kotlin.CoroutineCrudRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

/**
 * Created by jofisaes on 19/12/2021
 */
@MappedEntity(namingStrategy = NamingStrategies.UnderScoreSeparatedLowerCase::class)
data class TicketReservation(
    @field: Id
    @field: AutoPopulated
    var id: UUID? = null,
    val reference: UUID? = UUID.randomUUID(),
    val name: String,
    val address: String,
    val birthDate: LocalDate,
    @field: Relation(value = Relation.Kind.ONE_TO_ONE, cascade = [Relation.Cascade.ALL])
    val parkingReservation: ParkingReservation? = null,
    @field:DateCreated
    val createdAt: LocalDateTime? = LocalDateTime.now(),
)

@R2dbcRepository(dialect = POSTGRES)
interface TicketRepository : CoroutineCrudRepository<TicketReservation, UUID>,
    CoroutineJpaSpecificationExecutor<TicketReservation> {

    @Join(value = "parkingReservation", type = Join.Type.OUTER)
    override suspend fun findById(id: UUID): TicketReservation
}