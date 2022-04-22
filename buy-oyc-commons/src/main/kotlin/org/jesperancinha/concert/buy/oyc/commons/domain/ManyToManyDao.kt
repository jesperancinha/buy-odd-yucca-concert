package org.jesperancinha.concert.buy.oyc.commons.domain

import io.micronaut.data.annotation.*
import io.micronaut.data.model.naming.NamingStrategies
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.r2dbc.annotation.R2dbcRepository
import io.micronaut.data.repository.jpa.kotlin.CoroutineJpaSpecificationExecutor
import io.micronaut.data.repository.kotlin.CoroutineCrudRepository
import java.util.*

/**
 * Created by jofisaes on 05/04/2022
 */
@MappedEntity(namingStrategy = NamingStrategies.UnderScoreSeparatedLowerCase::class)
data class TicketReservationConcertDay(
    @field: Id
    @field: AutoPopulated
    var id: UUID? = null,
    @field: Relation(value = Relation.Kind.ONE_TO_ONE, cascade = [Relation.Cascade.ALL])
    val ticketReservation: TicketReservation,
    @field: Relation(value = Relation.Kind.ONE_TO_ONE, cascade = [Relation.Cascade.ALL])
    val concertDay: ConcertDayReservation
)

@R2dbcRepository(dialect = Dialect.POSTGRES)
interface TicketReservationConcertRepository : CoroutineCrudRepository<TicketReservationConcertDay, UUID>,
    CoroutineJpaSpecificationExecutor<TicketReservationConcertDay> {

    @Join(value = "ticketReservation", type = Join.Type.FETCH)
    @Join(value = "concertDay", type = Join.Type.FETCH)
    override suspend fun findById(id: UUID): TicketReservationConcertDay
}
