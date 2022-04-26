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
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.jesperancinha.concert.buy.oyc.commons.domain.*
import org.jesperancinha.concert.buy.oyc.commons.dto.ResponseDto
import org.jesperancinha.concert.buy.oyc.commons.dto.TicketDto
import org.jesperaninha.concert.buy.oyc.containers.AbstractContainersTest
import org.jesperaninha.concert.buy.oyc.containers.CustomContextBuilder
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.Thread.sleep
import java.net.URL
import java.time.LocalDate

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
        val serviceHost = dockerCompose.getServiceHost("kong_1", 8000)
        val httpClient = HttpClient.create(URL("http://$serviceHost:8000"))

        val ticketDto = TicketDto(name = "name", address = "address", birthDate = LocalDate.now())
        val dtoSingle = httpClient.retrieve(
            HttpRequest.POST("/api/yucca-api/api", ticketDto).header(
                ACCEPT, APPLICATION_JSON
            ), ResponseDto::class.java
        )

        dtoSingle.awaitFirst()

        withContext(Dispatchers.IO) {
            sleep(5000)
        }

        withContext(Dispatchers.IO) {
            receiptRepository.findAll().toList().shouldHaveSize(1)
            auditLogRepository.findAll().toList().shouldHaveAtMostSize(1)
            ticketRepository.findAll().toList().shouldHaveSize(1)
        }
    }

    companion object {
        @AfterAll
        fun tearDown() {
            dockerCompose.stop()
        }
    }
}
