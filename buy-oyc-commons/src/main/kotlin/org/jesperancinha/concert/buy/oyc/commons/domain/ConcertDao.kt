package org.jesperancinha.concert.buy.oyc.commons.domain

import io.micronaut.data.annotation.*
import io.micronaut.data.model.naming.NamingStrategies
import io.micronaut.data.model.naming.NamingStrategies.UnderScoreSeparatedLowerCase
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.r2dbc.annotation.R2dbcRepository
import io.micronaut.data.repository.jpa.kotlin.CoroutineJpaSpecificationExecutor
import io.micronaut.data.repository.kotlin.CoroutineCrudRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

/**
 * Created by jofisaes on 25/02/2022
 */
@MappedEntity(namingStrategy = UnderScoreSeparatedLowerCase::class)
data class ConcertDay(
    @field: Id
    @field: AutoPopulated
    var id: UUID? = null,
    val name: String,
    val description: String,
    @field:DateCreated
    val concertDate: LocalDate,
    @field:DateCreated
    val createdAt: LocalDateTime? = LocalDateTime.now()
)

@MappedEntity(namingStrategy = UnderScoreSeparatedLowerCase::class)
data class ConcertDayReservation(
    @field: Id
    @field: AutoPopulated
    var id: UUID? = null,
    val reference: UUID? = UUID.randomUUID(),
    @field: Relation(value = Relation.Kind.ONE_TO_ONE, cascade = [Relation.Cascade.ALL])
    val concert: ConcertDay,
    @field:DateCreated
    val createdAt: LocalDateTime? = LocalDateTime.now()
)

@R2dbcRepository(dialect = Dialect.POSTGRES)
interface ConcertDayRepository : CoroutineCrudRepository<ConcertDay, UUID>,
    CoroutineJpaSpecificationExecutor<ConcertDay>

@R2dbcRepository(dialect = Dialect.POSTGRES)
interface ConcertDayReservationRepository : CoroutineCrudRepository<ConcertDayReservation, UUID>,
    CoroutineJpaSpecificationExecutor<ConcertDayReservation> {
    @Join(value = "concert", type = Join.Type.FETCH)
    override suspend fun findById(id: UUID): ConcertDayReservation
}
