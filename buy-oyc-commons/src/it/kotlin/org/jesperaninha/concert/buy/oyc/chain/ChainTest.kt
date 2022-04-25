package org.jesperaninha.concert.buy.oyc.chain

import io.micronaut.context.DefaultApplicationContextBuilder
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.jesperancinha.concert.buy.oyc.commons.domain.*
import org.jesperaninha.concert.buy.oyc.containers.AbstractContainersTest
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

/**
 * Created by jofisaes on 22/04/2022
 */
@MicronautTest(contextBuilder = [CustomContextBuilder::class])
class ChainTest @Inject constructor(
    val auditLogRepository: AuditLogRepository,
    val receiptRepository: ReceiptRepository,
    val ticketRepository: TicketRepository,
    val concertDayReservationRepository: ConcertDayReservationRepository,
    val parkingReservationRepository: ParkingReservationRepository,
    val drinkReservationRepository: DrinkReservationRepository,
    val mealReservationRepository: MealReservationRepository,
) : AbstractContainersTest() {

    @Test
    fun `should run chain test and create a concert reservation`() {
    }
}

class CustomContextBuilder : DefaultApplicationContextBuilder() {
    init {
        eagerInitSingletons(true)
        val serviceHost = AbstractContainersTest.dockerCompose.getServiceHost("db_1", 5432)
        val props = mapOf(
            "r2dbc.datasources.default.url" to "r2dbc:postgresql://kong@$serviceHost:5432/yucca?currentSchema=ticket"
        )
        logger.info("Database Host configuration is $props")

        properties(props)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(CustomContextBuilder::class.java)
    }
}