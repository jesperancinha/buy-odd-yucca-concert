package org.jesperaninha.concert.buy.oyc.chain

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
import java.lang.Thread.sleep
import java.math.BigDecimal
import java.net.URL
import java.time.Duration.ofSeconds
import java.time.LocalDate

private const val DELAY: Long = 20

/**
 * Created by jofisaes on 22/04/2022
 */
@ExperimentalCoroutinesApi
@MicronautTest(contextBuilder = [CustomContextBuilder::class])
class ChainTest @Inject constructor(
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
        withContext(Dispatchers.IO) {
            sleep(ofSeconds(DELAY).toMillis())
        }

        val (serviceHost, servicePort) = dockerCompose.getContainerByServiceName("kong_1").get().let {
            it.host to it.firstMappedPort
        }

        val httpClient = HttpClient.create(URL("http://$serviceHost:$servicePort"))


        withContext(Dispatchers.IO) {
            delay(ofSeconds(DELAY).toMillis())
        }

        val drink = drinkRepository.save(
            Drink(
                name = "BlueYellow",
                width = 5,
                height = 5,
                shape = "cylinder",
                volume = DELAY,
                price = BigDecimal.TEN
            )
        )

        val meal = mealRepository.save(
            Meal(
                boxType = BoxType.XS,
                discount = 10,
                price = BigDecimal.TEN
            )
        )


        val concertDay = concertDayRepository.save(
            ConcertDay(
                name = "Kyiv Symphony Orchestra",
                description = "Peace concert",
                concertDate = LocalDate.now()
            )
        )

        val carParking = parkingRepository.save(
            CarParking(
                parkingNumber = 8
            )
        )
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
            delay(ofSeconds(DELAY).toMillis())
        }

        withContext(Dispatchers.IO) {
            delay(ofSeconds(DELAY).toMillis())
            receiptRepository.findAll().toList().shouldHaveSize(1)
            ticketReservationRepository.findAll().toList().shouldHaveSize(1)
            drinkReservationRepository.findAll().toList().shouldHaveSize(1)
            mealReservationRepository.findAll().toList().shouldHaveSize(1)
            concertDayReservationRepository.findAll().toList().shouldHaveSize(1)
            parkingReservationRepository.findAll().toList().shouldHaveSize(1)
            auditLogRepository.findAll().toList().shouldHaveAtMostSize(5)
        }
    }

    companion object {
        @AfterAll
        fun tearDown() {
            dockerCompose.stop()
        }
    }
}
