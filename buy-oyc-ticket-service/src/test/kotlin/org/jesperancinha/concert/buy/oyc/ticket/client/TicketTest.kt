package org.jesperancinha.concert.buy.oyc.ticket.client

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import io.kotest.matchers.longs.shouldBeLessThan
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
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.flywaydb.core.Flyway
import org.jesperancinha.concert.buy.oyc.commons.domain.*
import org.jesperancinha.concert.buy.oyc.commons.dto.TicketDto
import org.jesperancinha.concert.buy.oyc.ticket.containers.AbstractBuyOddYuccaConcertContainerTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit.NANOS
import java.util.*
import javax.transaction.Transactional

private const val API_YUCCA_TICKET = "/api/yucca-ticket"


/**
 * Created by jofisaes on 10/04/2022
 */
@ExperimentalCoroutinesApi
@MicronautTest
@Property(name = "buy.oyc.drink.port", value = "7999")
@Property(name = "buy.oyc.meal.port", value = "7998")
@Property(name = "buy.oyc.concert.port", value = "7997")
@Property(name = "buy.oyc.parking.port", value = "7996")
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
        stubResponse(
            API_YUCCA_TICKET, jacksonMapper
                .writeValueAsString(ticketDto), 200
        )
        wireMockServer1.start()
        wireMockServer2.start()
        wireMockServer3.start()
        wireMockServer4.start()
    }

    @Test
    @Transactional
    fun `should find all with an empty list`() = runTest {
        val reference = UUID.randomUUID()
        val testTicket = TicketDto(
            name = "name",
            reference = reference,
            address = "address",
            birthDate = LocalDate.now()
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
        val awaitFirstTicketDto = findAll.awaitFirst()
        awaitFirstTicketDto.reference shouldBe reference

//        val add = ticketReactiveClient.add(testTicketDto)
//        val blockingGet = withContext(Dispatchers.IO) {
//            add.blockingGet()
//        }
//        blockingGet["second"].shouldBe("Saved successfully !")
//        val findAll2 = ticketReactiveClient.getAllTickets()
//        findAll2.shouldNotBeNull()
//        findAll2.subscribe()
//        val awaitFirstReceiptDto2 = withContext(Dispatchers.IO) {
//            findAll2.toIterable()
//        }.toList()
//        awaitFirstReceiptDto2.shouldHaveSize(2)
//        withContext(Dispatchers.IO) {
//            sleep(1000)
//        }
//        auditLogRepository.findAll().toList().shouldHaveSize(1)
    }

    companion object {
        private val wireMockServer1 = WireMockServer(WireMockConfiguration().port(7999))
        private val wireMockServer2 = WireMockServer(WireMockConfiguration().port(7998))
        private val wireMockServer3 = WireMockServer(WireMockConfiguration().port(7997))
        private val wireMockServer4 = WireMockServer(WireMockConfiguration().port(7996))

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
            wireMockServer1.stop()
        }
    }

    private fun stubResponse(
        url: String,
        body: String,
        status: Int = HttpStatus.OK.code
    ) {
        wireMockServer1.stubFor(
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
}