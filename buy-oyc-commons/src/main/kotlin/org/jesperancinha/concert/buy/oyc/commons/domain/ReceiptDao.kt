package org.jesperancinha.concert.buy.oyc.commons.domain

import io.micronaut.data.annotation.AutoPopulated
import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.model.naming.NamingStrategies.UnderScoreSeparatedLowerCase
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.r2dbc.annotation.R2dbcRepository
import io.micronaut.data.repository.jpa.kotlin.CoroutineJpaSpecificationExecutor
import io.micronaut.data.repository.kotlin.CoroutineCrudRepository
import java.time.LocalDateTime
import java.util.*

/**
 * Created by jofisaes on 30/03/2022
 */
@MappedEntity(value = "receipt", namingStrategy = UnderScoreSeparatedLowerCase::class)
data class Receipt(
    @field: Id
    @field: AutoPopulated
    var id: UUID? = null,
    @field: AutoPopulated
    var reference: UUID? = null,
    @field:DateCreated
    var createdAt: LocalDateTime? = null,
    val ticketReservation: TicketReservation? = null
)

@R2dbcRepository(dialect = Dialect.POSTGRES)
interface ReceiptRepository : CoroutineCrudRepository<Receipt, UUID>,
    CoroutineJpaSpecificationExecutor<Receipt>
