package org.jesperancinha.concert.buy.oyc.commons.domain

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
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
import org.jesperancinha.concert.buy.oyc.containers.AbstractBuyOddYuccaConcertContainerTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Testcontainers
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.transaction.Transactional

/**
 * Created by jofisaes on 25/02/2022
 */
@Testcontainers
@MicronautTest
@ExperimentalCoroutinesApi
class TicketReservationTest @Inject constructor(
    private val ticketReservationRepository: TicketReservationRepository,
    private val parkingReservationRepository: ParkingReservationRepository,
    private val carParkingRepository: CarParkingRepository,
    private val concertDayReservationRepository: ConcertDayReservationRepository,
    private val concertDayRepository: ConcertDayRepository,
    private val drinkRepository: DrinkRepository,
    private val mealRepository: MealRepository,
    private val ticketReservationConcertRepository: TicketReservationConcertRepository
) : AbstractBuyOddYuccaConcertContainerTest() {

    private val config = ClassicConfiguration()

    @BeforeEach
    fun beforeEach() {
        config.isCleanDisabled = false
        postgreSQLContainer.start()
        config.setDataSource(postgreSQLContainer.jdbcUrl, postgreSQLContainer.username, postgreSQLContainer.password)
        config.schemas = arrayOf("ticket")
        Flyway(config).migrate()
    }

    @Test
    fun `should read an empty ticket list from repository`() = runTest {
        ticketReservationRepository.findAll().toList().shouldBeEmpty()
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
        val (id, reference, name, address, birthDateResult, carParkingTicket, createdAt) = ticketReservationRepository.save(
            ticketReservation
        )
        id.shouldNotBeNull()
        name shouldBe "João"
        reference.shouldNotBeNull()
        address shouldBe "Road to nowhere"
        birthDateResult shouldBeEqualComparingTo birthDate
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
        val (idParkingTicket, _, carParkingOnReservation) = savedParkingReservation

        idParkingTicket.shouldNotBeNull()
        carParkingOnReservation.shouldNotBeNull()
        parkingReservation.shouldBeEqualToComparingFields(savedParkingReservation)
        carParkingOnReservation.parkingNumber shouldBe 10
        carParkingOnReservation.id shouldBe carParkingResult.id
        carParkingOnReservation.parkingNumber shouldBe carParkingResult.parkingNumber

        val concertDaySaved = concertDayRepository.save(
            ConcertDay(
                name = "Pumpkin Halloween Tour",
                description = "The Veggies 4th Tour",
                concertDate = LocalDate.now()
            )
        )

        concertDaySaved.shouldNotBeNull()

        val concertDayReservation = concertDayReservationRepository.save(
            ConcertDayReservation(
                reference = UUID.randomUUID(),
                concert = concertDaySaved
            )
        )

        concertDayReservationRepository.save(
            ConcertDayReservation(
                reference = UUID.randomUUID(),
                concert = concertDaySaved
            )
        )

        concertDayReservation.shouldNotBeNull()

        val birthDate = LocalDate.now()
        val ticketReservation = TicketReservation(
            name = "João",
            birthDate = birthDate,
            address = "Road to nowhere",
            parkingReservation = savedParkingReservation,
        )
        val (id, reference, name, address, birthDateResult, carParkingTicketResult, createdAt) = ticketReservationRepository.save(
            ticketReservation
        )
        id.shouldNotBeNull()
        val reservation = ticketReservationRepository.findById(id)

        concertDayReservation.shouldNotBeNull()
        reservation.shouldNotBeNull()

        val ticketReservationConcertDay = ticketReservationConcertRepository
            .save(
                TicketReservationConcertDay(
                    ticketReservation = reservation,
                    concertDay = concertDayReservation
                )
            )
        ticketReservationConcertDay.shouldNotBeNull()
        ticketReservationConcertDay.ticketReservation shouldBe reservation
        ticketReservationConcertDay.concertDay shouldBe concertDayReservation

        val ticketReservationId = ticketReservationConcertDay.id
        ticketReservationId.shouldNotBeNull()
        val finalTicketReservationConcert = ticketReservationConcertRepository.update(ticketReservationConcertDay)
        val finalTicketReservationConcertId = finalTicketReservationConcert.id
        val final2TicketReservation =
            finalTicketReservationConcertId?.let {
                ticketReservationConcertRepository.findById(it)
            }
        final2TicketReservation.shouldNotBeNull()
        final2TicketReservation.ticketReservation.shouldNotBeNull()
        final2TicketReservation.concertDay.shouldNotBeNull()

        finalTicketReservationConcert.shouldNotBeNull()
        finalTicketReservationConcert.ticketReservation.shouldNotBeNull()
        finalTicketReservationConcert.concertDay.shouldNotBeNull()

        val drink = drinkRepository.save(
            Drink(
                name = "Orange Flavour",
                width = 5,
                height = 10,
                shape = "bottle",
                volume = 33,
                price = BigDecimal(10),
            )
        )

        val meal1 = mealRepository.save(
            Meal(
                boxType = XL,
                discount = 10,
                price = BigDecimal(80),
            )
        )

        val meal2 = mealRepository.save(
            Meal(
                boxType = XL,
                discount = 10,
                price = BigDecimal(80),
            )
        )

        val finalDrink = drink.id?.let {
            drinkRepository.findById(it)
        }
        val finalMeal1 = meal1.id?.let {
            mealRepository.findById(it)
        }
        val finalMeal2 = meal2.id?.let {
            mealRepository.findById(it)
        }
        finalDrink.shouldNotBeNull()
        finalMeal1.shouldNotBeNull()
        finalMeal2.shouldNotBeNull()
        reservation.shouldNotBeNull()
        val ticketReservationFinalId = reservation.id
        ticketReservationFinalId.shouldNotBeNull()
        reservation.parkingReservation.shouldNotBeNull()

        id.shouldNotBeNull()
        name shouldBe "João"
        reference.shouldNotBeNull()
        address shouldBe "Road to nowhere"
        birthDateResult shouldBeEqualComparingTo birthDate
        createdAt.shouldNotBeNull()
        carParkingTicketResult.shouldNotBeNull()
        carParkingTicketResult.id.shouldNotBeNull()
        idParkingTicket.shouldNotBeNull()
        carParkingOnReservation.shouldNotBeNull()
        carParkingOnReservation.parkingNumber shouldBe 10

        val newTicketReservation =
            ticketReservationRepository.update(
                reservation.copy(
                    createdAt = LocalDateTime.now()
                )
            )
        val finalTicketReservation =
            newTicketReservation.id?.let { ticketReservationRepository.findById(it) }
        finalTicketReservation.shouldNotBeNull()
        finalTicketReservation.parkingReservation.shouldNotBeNull()

        concertDayReservation.shouldNotBeNull()
        val concertDayReservationId = concertDayReservation.id
        concertDayReservationId.shouldNotBeNull()
        val concertDayReservationFinal = concertDayReservationRepository.findById(concertDayReservationId)
        concertDayReservationFinal.shouldNotBeNull()
        concertDayReservationFinal.concert.shouldNotBeNull()
        concertDayReservationRepository.findAll().toList().shouldHaveSize(2)

        finalTicketReservationConcertId.shouldNotBeNull()
        val ticketReservationConcertFinal =
            ticketReservationConcertRepository.findById(finalTicketReservationConcertId)
        ticketReservationConcertFinal.shouldNotBeNull()
        ticketReservationConcertFinal.ticketReservation.shouldNotBeNull()
        ticketReservationConcertFinal.concertDay.shouldNotBeNull()
    }

    @AfterEach
    fun clean() {
        Flyway(config).clean()
    }
}