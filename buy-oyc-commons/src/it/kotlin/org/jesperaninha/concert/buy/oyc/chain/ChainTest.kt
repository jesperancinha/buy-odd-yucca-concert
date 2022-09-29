@file:OptIn(ExperimentalKotest::class, ExperimentalKotest::class)

package org.jesperaninha.concert.buy.oyc.chain

import io.kotest.common.ExperimentalKotest
import io.kotest.framework.concurrency.FixedInterval
import io.kotest.framework.concurrency.eventually
import io.kotest.matchers.collections.shouldHaveAtMostSize
import io.kotest.matchers.collections.shouldHaveSize
import io.micronaut.http.HttpHeaders.ACCEPT
import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType.APPLICATION_JSON
import io.micronaut.http.client.HttpClient
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.jesperancinha.concert.buy.oyc.commons.domain.*
import org.jesperancinha.concert.buy.oyc.commons.dto.*
import org.jesperaninha.concert.buy.oyc.containers.AbstractContainersTest
import org.jesperaninha.concert.buy.oyc.containers.CustomContextBuilder
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.net.URL
import java.time.Duration.ofSeconds
import java.time.LocalDate

private const val DELAY: Long = 20
private const val SMALL_DELAY: Long = 1

/**
 * Created by jofisaes on 22/04/2022
 */
@ExperimentalCoroutinesApi
@MicronautTest(contextBuilder = [CustomContextBuilder::class])
open class ChainTest @Inject constructor(
    private val auditLogRepository: AuditLogRepository,
    private val receiptRepository: ReceiptRepository,
    private val ticketReservationRepository: TicketReservationRepository,
    private val concertDayRepository: ConcertDayRepository,
    private val concertDayReservationRepository: ConcertDayReservationRepository,
    private val parkingRepository: CarParkingRepository,
    private val parkingReservationRepository: ParkingReservationRepository,
    private val drinkRepository: DrinkRepository,
    private val drinkReservationRepository: DrinkReservationRepository,
    private val mealRepository: MealRepository,
    private val mealReservationRepository: MealReservationRepository,
) : AbstractContainersTest() {

    @BeforeEach
    fun `setup project`() = runTest {
        receiptRepository.deleteAll()
        auditLogRepository.deleteAll()
        ticketReservationRepository.deleteAll()
        concertDayRepository.deleteAll()
        concertDayReservationRepository.deleteAll()
        parkingRepository.deleteAll()
        parkingReservationRepository.deleteAll()
        drinkRepository.deleteAll()
        drinkReservationRepository.deleteAll()
        mealRepository.deleteAll()
        mealReservationRepository.deleteAll()
    }

    @Test
    fun `should run chain test and create a concert reservation`() = runTest {
        val (serviceHost, servicePort) = dockerCompose.getContainerByServiceName("kong_1").get().let {
            it.host to it.firstMappedPort
        }

        val httpClient = HttpClient.create(URL("http://$serviceHost:$servicePort"))

        val drink = withContext(Dispatchers.IO) {
            drinkRepository.save(
                Drink(
                    name = "BlueYellow",
                    width = 5,
                    height = 5,
                    shape = "cylinder",
                    volume = DELAY,
                    price = BigDecimal.TEN
                )
            )
        }

        assertWithTries { drinkRepository.findAll().toList().shouldHaveSize(1) }


        val meal = withContext(Dispatchers.IO) {
            mealRepository.save(
                Meal(
                    boxType = BoxType.XS,
                    discount = 10,
                    price = BigDecimal.TEN
                )
            )
        }

        assertWithTries { mealRepository.findAll().toList().shouldHaveSize(1) }

        val concertDay = withContext(Dispatchers.IO) {
            concertDayRepository.save(
                ConcertDay(
                    name = "Kyiv Symphony Orchestra",
                    description = "Peace concert",
                    concertDate = LocalDate.now()
                )
            )
        }

        assertWithTries { concertDayRepository.findAll().toList().shouldHaveSize(1) }

        val carParking = withContext(Dispatchers.IO) {
            parkingRepository.save(
                CarParking(
                    parkingNumber = 8
                )
            )
        }

        assertWithTries { parkingRepository.findAll().toList().shouldHaveSize(1) }

        val ticketDto = TicketDto(
            name = "name", address = "address", birthDate = LocalDate.now(),
            drinks = listOf(
                DrinkDto(
                    drinkId = drink.id
                )
            ),
            meals = listOf(
                MealDto(
                    mealId = meal.id
                )
            ),
            concertDays = listOf(
                ConcertDayDto(
                    concertId = concertDay.id
                )
            ),
            parkingReservation = ParkingReservationDto(
                carParkingId = carParking.parkingNumber
            )
        )
        val dtoSingle = httpClient.retrieve(
            HttpRequest.POST("/api/yucca-api/api", ticketDto).header(
                ACCEPT, APPLICATION_JSON
            ), ResponseDto::class.java
        )

        dtoSingle.awaitFirst()

        withContext(Dispatchers.IO) {
            assertWithTries { receiptRepository.findAll().toList().shouldHaveSize(1) }
            assertWithTries { ticketReservationRepository.findAll().toList().shouldHaveSize(1) }
            assertWithTries { drinkReservationRepository.findAll().toList().shouldHaveSize(1) }
            assertWithTries { mealReservationRepository.findAll().toList().shouldHaveSize(1) }
            assertWithTries { concertDayReservationRepository.findAll().toList().shouldHaveSize(2) }
            assertWithTries { parkingReservationRepository.findAll().toList().shouldHaveSize(1) }
            assertWithTries { auditLogRepository.findAll().toList().shouldHaveAtMostSize(5) }
        }
    }

    companion object {
        @JvmStatic
        @AfterAll
        fun tearDown() {
            dockerCompose.stop()
        }
    }
}

@OptIn(ExperimentalKotest::class)
private suspend fun <T> assertWithTries(function: suspend () -> Collection<T>) {
    eventually({
        duration = 60000
        interval = FixedInterval(1000)
    }) {
        function()
        withContext(Dispatchers.IO) {
            delay(ofSeconds(SMALL_DELAY).toMillis())
        }
    }
}
