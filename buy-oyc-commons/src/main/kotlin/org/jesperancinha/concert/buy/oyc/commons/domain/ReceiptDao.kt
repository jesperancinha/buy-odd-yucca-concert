package org.jesperancinha.concert.buy.oyc.commons.domain

import io.micronaut.data.annotation.*
import io.micronaut.data.model.naming.NamingStrategies
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.r2dbc.annotation.R2dbcRepository
import io.micronaut.data.repository.jpa.kotlin.CoroutineJpaSpecificationExecutor
import io.micronaut.data.repository.kotlin.CoroutineCrudRepository
import java.time.LocalDateTime
import java.util.*

/**
 * Created by jofisaes on 30/03/2022
 */
@MappedEntity("receipt", namingStrategy = NamingStrategies.UnderScoreSeparatedLowerCase::class)
data class Receipt(
    @field: Id
    @field: AutoPopulated
    val id: UUID? = null,
    @field:DateCreated
    val createdAt: LocalDateTime? = LocalDateTime.now(),
    @field: Relation(value = Relation.Kind.ONE_TO_ONE, cascade = [Relation.Cascade.PERSIST])
    val ticketReservation: TicketReservation
)


@R2dbcRepository(dialect = Dialect.POSTGRES)
interface ReceiptRepository : CoroutineCrudRepository<Receipt, UUID>,
    CoroutineJpaSpecificationExecutor<Receipt>