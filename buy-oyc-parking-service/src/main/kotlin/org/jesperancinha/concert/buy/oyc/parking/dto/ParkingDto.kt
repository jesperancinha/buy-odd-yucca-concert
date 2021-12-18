package org.jesperancinha.concert.buy.oyc.parking.dto

import org.jesperancinha.concert.buy.oyc.commons.domain.ParkingReservation

data class ParkingReservationDto(
    val pN: Long
)

val ParkingReservation.toDto
    get() = ParkingReservationDto(pN = parkingNumber)


val ParkingReservationDto.toData: ParkingReservation
    get() = ParkingReservation(parkingNumber = pN)
