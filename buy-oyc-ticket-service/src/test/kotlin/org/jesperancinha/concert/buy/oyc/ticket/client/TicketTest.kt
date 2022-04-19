package org.jesperancinha.concert.buy.oyc.ticket.client

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.micronaut.context.annotation.Property
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.flywaydb.core.Flyway
import org.jesperancinha.concert.buy.oyc.commons.domain.AuditLogRepository
import org.jesperancinha.concert.buy.oyc.commons.domain.TicketRepository
import org.jesperancinha.concert.buy.oyc.commons.dto.ConcertDayDto
import org.jesperancinha.concert.buy.oyc.commons.dto.DrinkDto
import org.jesperancinha.concert.buy.oyc.commons.dto.MealDto
import org.jesperancinha.concert.buy.oyc.commons.dto.TicketDto
import org.jesperancinha.concert.buy.oyc.ticket.containers.AbstractBuyOddYuccaConcertContainerTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.Thread.sleep
import java.time.LocalDate
import java.util.*
import javax.transaction.Transactional

private const val API_YUCCA_CATERING_DRINK = "/api/yucca-catering/drink"
private const val API_YUCCA_CATERING_MEAL = "/api/yucca-catering/meal"
private const val API_YUCCA_CONCERT = "/api/yucca-concert"
private const val API_YUCCA_PARKING = "/api/yucca-parking"


/**
 * Created by jofisaes on 10/04/2022
 */
@ExperimentalCoroutinesApi
@MicronautTest
@Property(name = "buy.oyc.catering.port", value = "7999")
@Property(name = "buy.oyc.concert.port", value = "7998")
@Property(name = "buy.oyc.parking.port", value = "7997")
class TicketTest @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val ticketReactiveClient: TicketReactiveClient,
    private val auditLogRepository: AuditLogRepository
) : AbstractBuyOddYuccaConcertContainerTest() {


    private val jacksonMapper = jacksonObjectMapper()
        .registerModule(JavaTimeModule())

    @BeforeEach
    fun setUpEach() = runTest {
        ticketRepository.deleteAll()
        val ticketDto = TicketDto(name = "name", address = "address", birthDate = LocalDate.now())
        wireMockServerCatering.stubResponse(
            API_YUCCA_CATERING_DRINK, jacksonMapper
                .writeValueAsString(
                    DrinkDto(
                        reference = UUID.randomUUID(),
                        drink = UUID.randomUUID()
                    )
                ), 200
        )
        wireMockServerCatering.stubResponse(
            API_YUCCA_CATERING_MEAL, jacksonMapper
                .writeValueAsString(
                    MealDto(
                        reference = UUID.randomUUID(),
                        drink = UUID.randomUUID()
                    )
                ), 200
        )
        wireMockServerConcert.stubResponse(
            API_YUCCA_CONCERT, jacksonMapper
                .writeValueAsString(
                    ConcertDayDto(
                        name = "concert",
                        reference = UUID.randomUUID(),
                        concertDate = LocalDate.now(),
                        description = "Camping with the campers"
                    )
                ), 200
        )
        wireMockServerParking.stubResponse(
            API_YUCCA_PARKING, jacksonMapper
                .writeValueAsString(ticketDto), 200
        )
        wireMockServerCatering.start()
        wireMockServerConcert.start()
        wireMockServerParking.start()
    }

    @Test
    @Transactional
    fun `should find all with an empty list`() = runTest {
        val reference = UUID.randomUUID()
        val concertDayDto = ConcertDayDto(
            name = "concert",
            reference = reference,
            concertDate = LocalDate.now(),
            description = "Camping with the campers"
        )
        val drinkDto = DrinkDto(
            reference = UUID.randomUUID(),
            drink = UUID.randomUUID()
        )
        val mealDto = MealDto(
            reference = UUID.randomUUID(),
            drink = UUID.randomUUID()
        )
        val testTicket = TicketDto(
            name = "name",
            reference = reference,
            address = "address",
            birthDate = LocalDate.now(),
            concertDays = listOf(concertDayDto),
            drinks = listOf(drinkDto),
            meals = listOf(mealDto)
        )
        val add = ticketReactiveClient.add(
            testTicket
        )
        val result = withContext(Dispatchers.IO) {
            add.blockingGet()
        }
        result["second"].shouldBe("Saved successfully !")

        val findAll = ticketReactiveClient.getAllTickets()
        findAll.shouldNotBeNull()
        findAll.subscribe {
            it.reference shouldBe reference
        }
        withContext(Dispatchers.IO) {
            sleep(1000)
        }
        val awaitFirstTicketDto = findAll.awaitFirst()
        awaitFirstTicketDto.reference shouldBe reference

        withContext(Dispatchers.IO) {
            sleep(1000)
        }
        auditLogRepository.findAll().toList().shouldHaveSize(3)
    }

    companion object {
        private val wireMockServerCatering = WireMockServer(WireMockConfiguration().port(7999))
        private val wireMockServerConcert = WireMockServer(WireMockConfiguration().port(7998))
        private val wireMockServerParking = WireMockServer(WireMockConfiguration().port(7997))

        @JvmStatic
        @BeforeAll
        fun setUp() {
            postgreSQLContainer.start()
            redis.start()
            config.setDataSource(
                postgreSQLContainer.jdbcUrl, postgreSQLContainer.username, postgreSQLContainer.password
            )
            config.schemas = arrayOf("ticket")
            Flyway(config).migrate()
        }

        @JvmStatic
        @AfterAll
        fun tearDown() {
            wireMockServerCatering.stop()
            wireMockServerConcert.stop()
            wireMockServerParking.stop()
        }
    }
}

private fun WireMockServer.stubResponse(
    url: String,
    body: String,
    status: Int = HttpStatus.OK.code
) {
    stubFor(
        WireMock.post(url)
            .willReturn(
                WireMock.aResponse()
                    .withStatus(status)
                    .withHeader(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON
                    )
                    .withBody(body)
            )
    )
}