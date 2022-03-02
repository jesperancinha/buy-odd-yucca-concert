package org.jesperancinha.concert.buy.oyc.parking.dto

import org.jesperancinha.concert.buy.oyc.commons.domain.CarParking
import org.jesperancinha.concert.buy.oyc.commons.domain.ParkingReservation

data class ParkingReservationDto(
    val pN: Long
)

val ParkingReservation.toDto
    get() = ParkingReservationDto(pN = carParking?.parkingNumber ?: -1)


val ParkingReservationDto.toData: ParkingReservation
    get() = ParkingReservation(carParking = CarParking(parkingNumber = pN))
