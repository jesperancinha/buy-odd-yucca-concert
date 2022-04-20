package org.jesperancinha.concert.buy.oyc.commons.dto

import org.jesperancinha.concert.buy.oyc.commons.domain.AuditLogType
import org.jesperancinha.concert.buy.oyc.commons.domain.BuyOycType
import org.jesperancinha.concert.buy.oyc.commons.domain.CarParking
import org.jesperancinha.concert.buy.oyc.commons.domain.ParkingReservation
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

/**
 * Created by jofisaes on 18/04/2022
 */
data class ParkingReservationDto(
    var reference: UUID? = UUID.randomUUID(),
    var carParkingId: Long,
    val createdAt: LocalDateTime? = LocalDateTime.now(),
    override val type: AuditLogType = AuditLogType.PARKING
) : Serializable, BuyOycType


val ParkingReservation.toDto: ParkingReservationDto
    get() = ParkingReservationDto(
        reference = reference,
        carParkingId = carParking?.parkingNumber ?: -1,
        createdAt = createdAt
    )

fun ParkingReservationDto.toData(carParking: CarParking): ParkingReservation = ParkingReservation(
    reference = reference,
    carParking = carParking,
    createdAt = createdAt
)
