package org.jesperancinha.concert.buy.oyc.commons.dto

import org.jesperancinha.concert.buy.oyc.commons.domain.AuditLogType
import org.jesperancinha.concert.buy.oyc.commons.domain.BuyOycType
import org.jesperancinha.concert.buy.oyc.commons.domain.ConcertDay
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

/**
 * Created by jofisaes on 20/04/2022
 */
data class ConcertDayDto(
    val name: String,
    var reference: UUID? = null,
    val description: String,
    val concertDate: LocalDate,
    val createdAt: LocalDateTime? = LocalDateTime.now(),
    override val type: AuditLogType = AuditLogType.CONCERT_DAY
) : Serializable, BuyOycType

val ConcertDay.toDto: ConcertDayDto
    get() = ConcertDayDto(
        name = name,
        reference = reference,
        description = description,
        concertDate = concertDate,
        createdAt = createdAt
    )
val ConcertDayDto.toData: ConcertDay
    get() = ConcertDay(
        name = name,
        reference = reference,
        description = description,
        concertDate = concertDate,
        createdAt = createdAt
    )