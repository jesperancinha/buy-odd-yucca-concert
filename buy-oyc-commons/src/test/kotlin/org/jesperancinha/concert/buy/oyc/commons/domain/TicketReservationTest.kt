@file:OptIn(ExperimentalCoroutinesApi::class)

package org.jesperancinha.concert.buy.oyc.commons.domain

import io.kotest.matchers.collections.shouldBeEmpty
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
import org.jesperancinha.concert.buy.oyc.commons.containers.AbstractContainerTest
import org.jesperancinha.concert.buy.oyc.commons.domain.BoxType.XL
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Testcontainers
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
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
    private val mealRepository: MealRepository,
    private val drinkReservationRepository: DrinkReservationRepository,
    private val mealReservationRepository: MealReservationRepository,
    private val ticketReservationConcertRepository: TicketReservationConcertRepository
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
        val (id, reference, name, address, birthDateResult, carParkingTicket, createdAt) = ticketRepository.save(
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

        val concertDay = concertDayRepository.save(
            ConcertDay(
                name = "Cabbage Maniacs",
                description = "Soul Music",
                concert_date = LocalDate.now()
            )
        )

        val birthDate = LocalDate.now()
        val ticketReservation = TicketReservation(
            name = "João",
            birthDate = birthDate,
            address = "Road to nowhere",
            parkingReservation = savedParkingReservation,
        )
        val (id, reference, name, address, birthDateResult, carParkingTicketResult, createdAt) = ticketRepository.save(
            ticketReservation
        )
        id.shouldNotBeNull()
        val reservation = ticketRepository.findById(id)

        concertDay.shouldNotBeNull()
        reservation.shouldNotBeNull()

        val ticketReservationConcertDay = ticketReservationConcertRepository
            .save(
                TicketReservationConcertDay(
                    ticketReservation = reservation,
                    concertDay = concertDay
                )
            )
        ticketReservationConcertDay.shouldNotBeNull()
        ticketReservationConcertDay.ticketReservation shouldBe reservation
        ticketReservationConcertDay.concertDay shouldBe concertDay

        val ticketReservationId = ticketReservationConcertDay.id
        ticketReservationId.shouldNotBeNull()
        val finalTicketReservationConcert = ticketReservationConcertRepository.update(ticketReservationConcertDay)
        val final2TicketReservation =
            finalTicketReservationConcert.id?.let {
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
        reservation.id.shouldNotBeNull()
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
            ticketRepository.update(
                reservation.copy(
                    createdAt = LocalDateTime.now()
                )
            )
        val finalTicketReservation =
            newTicketReservation.id?.let { ticketRepository.findById(it) }
        finalTicketReservation.shouldNotBeNull()
        finalTicketReservation.parkingReservation.shouldNotBeNull()

//        val ticketReservationConcertFinal =
//            ticketReservationConcertRepository.findByTicketReservation(reservation).id?.let {
//                ticketReservationConcertRepository.findById(it)
//            }
//        ticketReservationConcertFinal.shouldNotBeNull()
//        ticketReservationConcertFinal.ticketReservation.shouldNotBeNull()
//        ticketReservationConcertFinal.concertDay.shouldNotBeNull()
    }

    @AfterEach
    fun clean() {
        Flyway(config).clean()
    }
}