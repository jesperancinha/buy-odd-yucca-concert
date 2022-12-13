package org.jesperancinha.concert.buy.oyc.catering.client

import io.kotest.matchers.collections.shouldHaveSize
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.flywaydb.core.Flyway
import org.jesperancinha.concert.buy.oyc.commons.domain.*
import org.jesperancinha.concert.buy.oyc.commons.dto.DrinkDto
import org.jesperancinha.concert.buy.oyc.commons.dto.MealDto
import org.jesperancinha.concert.buy.oyc.containers.AbstractBuyOddYuccaConcertContainerTest
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.Thread.sleep
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import javax.transaction.Transactional

/**
 * Created by jofisaes on 21/04/2022
 */
@ExperimentalCoroutinesApi
@MicronautTest(transactional = false)
class CateringTest @Inject constructor(
    private val drinkRepository: DrinkRepository,
    private val drinkReservationRepository: DrinkReservationRepository,
    private val mealRepository: MealRepository,
    private val mealReservationRepository: MealReservationRepository,
    private val ticketReservationRepository: TicketReservationRepository,
    private val cateringReactiveClient: CateringReactiveClient,
) : AbstractBuyOddYuccaConcertContainerTest() {

    @BeforeEach
    fun setUpEach() = runTest {
        drinkRepository.deleteAll()
        drinkReservationRepository.deleteAll()
        mealRepository.deleteAll()
        mealReservationRepository.deleteAll()
        ticketReservationRepository.deleteAll()
        ticketReservationRepository.save(
            TicketReservation(
                reference = UUID.randomUUID(),
                name = "name",
                address = "address",
                birthDate = LocalDate.now()
            )
        )

        drinkRepository.save(
            Drink(
                name = "Loca Cola",
                width = 10,
                height = 10,
                shape = "cylinder",
                volume = 15,
                price = BigDecimal(10)

            )
        )
        mealRepository.save(
            Meal(
                coupon = UUID.randomUUID(),
                boxType = BoxType.L,
                discount = 10,
                price = BigDecimal(100)
            )
        )
    }

    @Test
    @Transactional
    fun `Should reserve meals and drinks`() = runTest {
        val (idTicket, reference, _, _, _, _, _) = ticketReservationRepository.findAll().first()
        val (idDrink, _, _, _, _, _, _, _) = drinkRepository.findAll().first()
        val (idMeal, _, _, _, _, _, _) = mealRepository.findAll().first()
        val addMeal = cateringReactiveClient.createMeal(
            MealDto(
                reference = reference,
                mealId = idMeal,
                ticketReservationId = idTicket
            )
        )
        addMeal.subscribe()
        val addDrink = cateringReactiveClient.createDrink(
            DrinkDto(
                reference = reference,
                drinkId = idDrink,
                ticketReservationId = idTicket
            )
        )
        addDrink.subscribe()

        withContext(Dispatchers.IO) {
            sleep(5000)
        }

        drinkReservationRepository.findAll().toList().shouldHaveSize(1)
        mealReservationRepository.findAll().toList().shouldHaveSize(1)
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun setUp() {
            config.isCleanDisabled = false
            postgreSQLContainer.start()
            redis.start()
            config.setDataSource(
                postgreSQLContainer.jdbcUrl, postgreSQLContainer.username, postgreSQLContainer.password
            )
            config.schemas = arrayOf("ticket")
            Flyway(config).migrate()
        }
    }
}