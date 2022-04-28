package org.jesperancinha.concert.buy.oyc.commons.dto

import org.jesperancinha.concert.buy.oyc.commons.domain.AuditLogType
import org.jesperancinha.concert.buy.oyc.commons.domain.BuyOycType
import org.jesperancinha.concert.buy.oyc.commons.domain.ConcertDay
import org.jesperancinha.concert.buy.oyc.commons.domain.ConcertDayReservation
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

/**
 * Created by jofisaes on 20/04/2022
 */
data class ConcertDayDto(
    var reference: UUID? = null,
    var concertId: UUID? = null,
    val createdAt: LocalDateTime? = null,
    override val type: AuditLogType = AuditLogType.CONCERT_DAY
) : Serializable, BuyOycType()

val ConcertDayReservation.toDto: ConcertDayDto
    get() = ConcertDayDto(
        reference = reference,
        concertId = concert?.id,
        createdAt = createdAt
    )

fun ConcertDayDto.toData(concertDay: ConcertDay) = ConcertDayReservation(
    reference = reference,
    concert = concertDay
)