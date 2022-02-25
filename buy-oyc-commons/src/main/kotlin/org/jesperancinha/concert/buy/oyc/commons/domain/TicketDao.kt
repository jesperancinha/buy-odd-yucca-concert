package org.jesperancinha.concert.buy.oyc.commons.domain

import io.micronaut.data.annotation.AutoPopulated
import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.model.naming.NamingStrategies
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.r2dbc.annotation.R2dbcRepository
import io.micronaut.data.repository.jpa.kotlin.CoroutineJpaSpecificationExecutor
import io.micronaut.data.repository.kotlin.CoroutineCrudRepository
import jakarta.persistence.Column
import java.net.Inet4Address
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

/**
 * Created by jofisaes on 19/12/2021
 */
@MappedEntity(value = "ticket_reservation", namingStrategy = NamingStrategies.UnderScoreSeparatedLowerCase::class)
data class TicketReservation(
    @field: Id
    @field: AutoPopulated
    val id: UUID? = null,
    val reference: UUID = UUID.randomUUID(),
    val name:String,
    val address: String,
    val birthDate:LocalDate,
    val concertDays: List<String> = emptyList(),
    val meals: List<String> = emptyList(),
    val carParkingTicket: String? = null,
    @field:DateCreated
    val createdAt: LocalDateTime? = LocalDateTime.now(),
)

@R2dbcRepository(dialect = Dialect.POSTGRES)
interface TicketRepository : CoroutineCrudRepository<TicketReservation, UUID>,
    CoroutineJpaSpecificationExecutor<TicketReservation>