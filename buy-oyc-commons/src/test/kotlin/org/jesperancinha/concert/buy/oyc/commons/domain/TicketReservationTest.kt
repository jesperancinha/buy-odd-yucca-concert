@file:OptIn(ExperimentalCoroutinesApi::class)

package org.jesperancinha.concert.buy.oyc.commons.domain

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.ClassicConfiguration
import org.jesperancinha.concert.buy.oyc.commons.domain.BoxType.XL
import org.jesperancinha.concert.buy.oyc.containers.AbstractContainerTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Testcontainers
import java.math.BigDecimal
import java.time.LocalDate
import javax.transaction.Transactional

/**
 * Created by jofisaes on 25/02/2022
 */
@Testcontainers
@MicronautTest
class TicketReservationTest @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val parkingReservationRepository: ParkingReservationRepository,
    private val carParkingRepository: CarParkingRepository,
    private val concertDayRepository: ConcertDayRepository,
    private val drinkRepository: DrinkRepository,
    private val mealRepository: MealRepository
) : AbstractContainerTest() {

    private val config = ClassicConfiguration()

    @BeforeEach
    fun beforeEach() {
        postgreSQLContainer.start()
        config.setDataSource(postgreSQLContainer.jdbcUrl, postgreSQLContainer.username, postgreSQLContainer.password)
        config.schemas = arrayOf("ticket")
        Flyway(config).migrate()
    }

    @Test
    fun `should read an empty ticket list from repository`() = runTest {
        ticketRepository.findAll().toList().shouldBeEmpty()
    }

    @Test
    @Transactional
    fun `should save simple ticket to repository`() = runTest {
        val birthDate = LocalDate.now()
        val ticketReservation = TicketReservation(
            name = "João",
            birthDate = birthDate,
            address = "Road to nowhere"
        )
        val (id, reference, name, address, birthDateResult, concertDays, meals, drinks, carParkingTicket, createdAt) = ticketRepository.save(
            ticketReservation
        )
        id.shouldNotBeNull()
        name shouldBe "João"
        reference.shouldNotBeNull()
        address shouldBe "Road to nowhere"
        birthDateResult shouldBeEqualComparingTo birthDate
        concertDays.shouldBeEmpty()
        meals.shouldBeEmpty()
        drinks.shouldBeEmpty()
        carParkingTicket.shouldBeNull()
        createdAt.shouldNotBeNull()
    }

    @Test
    @Transactional
    fun `should save complete ticket to repository`() = runTest {
        val carParking = CarParking(parkingNumber = 10)
        val carParkingResult = carParkingRepository.save(carParking)

        carParkingResult.shouldNotBeNull()
        carParkingResult.id.shouldNotBeNull()

        val parkingReservation = ParkingReservation(carParking = carParkingResult)
        val savedParkingReservation = parkingReservationRepository.save(parkingReservation)
        val (idParkingTicket, carParkingOnReservation) = savedParkingReservation

        idParkingTicket.shouldNotBeNull()
        carParkingOnReservation.shouldNotBeNull()
        parkingReservation.shouldBeEqualToComparingFields(savedParkingReservation)
        carParkingOnReservation.parkingNumber shouldBe 10
        carParkingOnReservation.id shouldBe carParkingResult.id
        carParkingOnReservation.parkingNumber shouldBe carParkingResult.parkingNumber

        val concertDay = concertDayRepository.save(
            ConcertDay(
                name = "Cabbage Maniacs",
                description = "Soul Music",
                date = LocalDate.now()
            )
        )

        val drink = drinkRepository.save(
            Drink(
                name = "Orange Flavour",
                width = 5,
                height = 10,
                shape = "bottle",
                volume = 33,
                price = BigDecimal(10)
            )
        )

        val meal = mealRepository.save(
            Meal(
                boxType = XL,
                discount = 10,
                price = BigDecimal(80)

            )
        )
        val birthDate = LocalDate.now()
        val ticketReservation = TicketReservation(
            name = "João",
            birthDate = birthDate,
            address = "Road to nowhere",
            parkingReservation = savedParkingReservation,
            concertDays = listOf(concertDay),
            drinks = listOf(drink),
            meals = listOf(meal)
        )
        val (id, reference, name, address, birthDateResult, concertDays, meals, drinks, carParkingTicketResult, createdAt) = ticketRepository.save(
            ticketReservation
        )
        id.shouldNotBeNull()
        name shouldBe "João"
        reference.shouldNotBeNull()
        address shouldBe "Road to nowhere"
        birthDateResult shouldBeEqualComparingTo birthDate
        concertDays.shouldHaveSize(1)
        val firstConcertDay = concertDays.first()
        firstConcertDay.shouldNotBeNull()
        firstConcertDay.name shouldBe concertDay.name
        firstConcertDay.description shouldBe concertDay.description
        firstConcertDay.date shouldBe concertDay.date
        drinks.shouldHaveSize(1)
        val firstDrink = drinks.first()
        firstDrink.shouldNotBeNull()
        firstDrink.name shouldBe "Orange Flavour"
        firstDrink.height shouldBe 10
        firstDrink.shape shouldBe "bottle"
        firstDrink.volume shouldBe 33
        firstDrink.price shouldBe BigDecimal(10)
        meals.shouldHaveSize(1)
        val firstMeal = meals.first()
        firstMeal.shouldNotBeNull()
        firstMeal.boxType shouldBe XL
        firstMeal.discount shouldBe 10
        firstMeal.price shouldBe BigDecimal(80)
        createdAt.shouldNotBeNull()
        carParkingTicketResult.shouldNotBeNull()
        carParkingTicketResult.idPR.shouldNotBeNull()
        parkingReservationRepository.findAll().toList().shouldNotBeEmpty()
        idParkingTicket.shouldNotBeNull()
        carParkingOnReservation.shouldNotBeNull()
        carParkingOnReservation.parkingNumber shouldBe 10
    }

    @AfterEach
    fun clean() {
        Flyway(config).clean()
    }
}