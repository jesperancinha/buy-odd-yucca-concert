package org.jesperancinha.concert.buy.oyc.commons.dto

import org.jesperancinha.concert.buy.oyc.commons.domain.AuditLogType
import org.jesperancinha.concert.buy.oyc.commons.domain.BuyOycType
import org.jesperancinha.concert.buy.oyc.commons.domain.ConcertDay
import org.jesperancinha.concert.buy.oyc.commons.domain.ConcertDayReservation
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

/**
 * Created by jofisaes on 20/04/2022
 */
data class ConcertDayDto(
    var reference: UUID? = null,
    val concertId: UUID? = null,
    val concertDate: LocalDate,
    val createdAt: LocalDateTime? = LocalDateTime.now(),
    override val type: AuditLogType = AuditLogType.CONCERT_DAY
) : Serializable, BuyOycType

val ConcertDayReservation.toDto: ConcertDayDto
    get() = ConcertDayDto(
        reference = reference,
        concertId = concert.id,
        concertDate = concertDate,
        createdAt = createdAt
    )

fun ConcertDayDto.toData(concertDay: ConcertDay) = ConcertDayReservation(
    reference = reference,
    concert = concertDay,
    concertDate = concertDate,
    createdAt = createdAt
)