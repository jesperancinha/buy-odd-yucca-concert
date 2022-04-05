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
@MappedEntity(
    value = "ticket_reservation_concert_day",
    namingStrategy = NamingStrategies.UnderScoreSeparatedLowerCase::class
)
data class TicketReservationConcert(
    @field: Id
    @field: AutoPopulated
    val id: UUID? = null,
    val reference: UUID = UUID.randomUUID(),
    @field: Relation(value = Relation.Kind.ONE_TO_ONE, cascade = [Relation.Cascade.PERSIST])
    val ticketReservation: TicketReservation,
    @field: Relation(value = Relation.Kind.ONE_TO_ONE, cascade = [Relation.Cascade.PERSIST])
    val concertDay: ConcertDay
)

@R2dbcRepository(dialect = Dialect.POSTGRES)
interface TicketReservationConcertRepository : CoroutineCrudRepository<TicketReservationConcert, UUID>,
    CoroutineJpaSpecificationExecutor<TicketReservationConcert>
