package org.jesperancinha.concert.buy.oyc.commons.domain

import io.micronaut.data.annotation.*
import io.micronaut.data.annotation.Relation.Cascade.PERSIST
import io.micronaut.data.annotation.Relation.Kind.ONE_TO_ONE
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
@MappedEntity(value = "audit_log", namingStrategy = NamingStrategies.UnderScoreSeparatedLowerCase::class)
data class AuditLog(
    @field: Id
    @field: AutoPopulated
    var id: UUID? = null,
    val auditLogType: AuditLogType,
    val payload: String,
    @field:DateCreated
    var createdAt: LocalDateTime? = null
)

enum class AuditLogType {
    TICKET,
    DRINK,
    MEAL,
    CONCERT_DAY,
    RECEIPT
}

@R2dbcRepository(dialect = Dialect.POSTGRES)
interface AuditLogRepository : CoroutineCrudRepository<AuditLog, UUID>,
    CoroutineJpaSpecificationExecutor<AuditLog>
