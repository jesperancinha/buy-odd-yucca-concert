package org.jesperaninha.concert.buy.oyc.chain

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.jesperancinha.concert.buy.oyc.commons.domain.*
import org.jesperaninha.concert.buy.oyc.containers.AbstractContainersTest
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

/**
 * Created by jofisaes on 22/04/2022
 */
@MicronautTest
class ChainTest @Inject constructor(
    val auditLogRepository: AuditLogRepository,
    val receiptRepository: ReceiptRepository,
    val ticketRepository: TicketRepository,
    val concertDayReservationRepository: ConcertDayReservationRepository,
    val parkingReservationRepository: ParkingReservationRepository,
    val drinkReservationRepository: DrinkReservationRepository,
    val mealReservationRepository: MealReservationRepository
) : AbstractContainersTest() {

    @Test
    fun `should run chain test and create a concert reservation`() {

    }
}