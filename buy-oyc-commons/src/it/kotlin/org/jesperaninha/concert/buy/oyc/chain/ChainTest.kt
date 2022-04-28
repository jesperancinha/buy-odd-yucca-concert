package org.jesperaninha.concert.buy.oyc.chain

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
import org.jesperancinha.concert.buy.oyc.commons.dto.DrinkDto
import org.jesperancinha.concert.buy.oyc.commons.dto.ResponseDto
import org.jesperancinha.concert.buy.oyc.commons.dto.TicketDto
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
    val auditLogRepository: AuditLogRepository,
    val receiptRepository: ReceiptRepository,
    val ticketRepository: TicketRepository,
    val concertDayReservationRepository: ConcertDayReservationRepository,
    val parkingReservationRepository: ParkingReservationRepository,
    val drinkRepository: DrinkRepository,
    val drinkReservationRepository: DrinkReservationRepository,
    val mealReservationRepository: MealReservationRepository,
) : AbstractContainersTest() {

    @BeforeEach
    fun `setup project`() = runTest {
        receiptRepository.deleteAll()
        auditLogRepository.deleteAll()
        ticketRepository.deleteAll()
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

        val ticketDto = TicketDto(
            name = "name", address = "address", birthDate = LocalDate.now(),
            drinks = listOf(
                DrinkDto(
                    drinkId = drink.id
                )
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
            ticketRepository.findAll().toList().shouldHaveSize(1)
            drinkReservationRepository.findAll().toList().shouldHaveSize(1)
            auditLogRepository.findAll().toList().shouldHaveSize(2)
        }
    }

    companion object {
        @AfterAll
        fun tearDown() {
            dockerCompose.stop()
        }
    }
}
