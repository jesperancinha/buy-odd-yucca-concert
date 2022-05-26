package org.jesperancinha.concert.buy.oyc.api.ticket.service

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import io.kotest.matchers.nulls.shouldNotBeNull
import io.micronaut.context.annotation.Property
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.jesperancinha.concert.buy.oyc.api.service.ReservationsService
import org.jesperancinha.concert.buy.oyc.commons.domain.Receipt
import org.jesperancinha.concert.buy.oyc.commons.domain.ReceiptRepository
import org.jesperancinha.concert.buy.oyc.commons.dto.TicketDto
import org.jesperancinha.concert.buy.oyc.containers.AbstractBuyOddYuccaConcertContainerTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.LocalDate
import javax.transaction.Transactional


private const val API_YUCCA_TICKET = "/api/yucca-ticket"

@ExperimentalCoroutinesApi
@Testcontainers
@MicronautTest
@Property(name = "buy.oyc.ticket.port", value = "7999")
@Property(name = "micronaut.server.port", value = "-1")
@DelicateCoroutinesApi
internal class ReservationsServiceTest @Inject constructor(
    private val reservationsService: ReservationsService,
    private val receiptRepository: ReceiptRepository
) : AbstractBuyOddYuccaConcertContainerTest() {

    private val jacksonMapper = jacksonObjectMapper()
        .registerModule(JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

    @BeforeEach
    fun setUpEach() {
        val ticketDto = TicketDto(name = "name", address = "address", birthDate = LocalDate.now())
        stubResponse(
            API_YUCCA_TICKET, jacksonMapper
                .writeValueAsString(ticketDto), 200
        )
    }

    @Test
    @Transactional
    fun `should save receipt directly`() = runTest {
        val receipt = receiptRepository.save(Receipt())
        receipt.id.shouldNotBeNull()
    }

    @Test
    @Transactional
    fun `should get all reservations`() = runTest {
        reservationsService.getAll().toList().shouldNotBeNull()
    }

    @Test
    @Transactional
    fun `should publish test`() = runTest {
        val testTicketDto = TicketDto(
            name = "name",
            address = "address",
            birthDate = LocalDate.now()
        )

        reservationsService.createTicket(
            testTicketDto
        )
    }

    companion object {
        private val wireMockServer = WireMockServer(WireMockConfiguration().port(7999))

        @JvmStatic
        @BeforeAll
        fun setUp() {
            redis.start()
            postgreSQLContainer.start()
            wireMockServer.start()
        }

        @JvmStatic
        @AfterAll
        fun tearDown() {
            wireMockServer.stop()
        }
    }

    private fun stubResponse(url: String, responseBody: String, responseStatus: Int = HttpStatus.OK.code) {
        wireMockServer.stubFor(
            post(url)
                .willReturn(
                    aResponse()
                        .withStatus(responseStatus)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .withBody(responseBody)
                )
        )
    }
}

