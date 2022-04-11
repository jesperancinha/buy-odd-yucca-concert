package org.jesperancinha.concert.buy.oyc.api.client

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import io.kotest.matchers.collections.shouldHaveSize
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
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.flywaydb.core.Flyway
import org.jesperancinha.concert.buy.oyc.api.containers.AbstractBuyOddYuccaConcertContainerTest
import org.jesperancinha.concert.buy.oyc.api.dto.TicketDto
import org.jesperancinha.concert.buy.oyc.commons.domain.AuditLogRepository
import org.jesperancinha.concert.buy.oyc.commons.domain.Receipt
import org.jesperancinha.concert.buy.oyc.commons.domain.ReceiptRepository
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.toFlux
import java.time.LocalDate
import java.time.temporal.ChronoUnit.NANOS
import javax.transaction.Transactional

private const val API_YUCCA_TICKET = "/api/yucca-ticket"


/**
 * Created by jofisaes on 10/04/2022
 */
@ExperimentalCoroutinesApi
@MicronautTest
@Property(name = "buy.oyc.ticket.port", value = "7999")
class ReceiptTest @Inject constructor(
    private val receiptRepository: ReceiptRepository,
    private val receiptReactiveClient: ReceiptReactiveClient,
    private val auditLogRepository: AuditLogRepository
) : AbstractBuyOddYuccaConcertContainerTest() {


    private val jacksonMapper = jacksonObjectMapper()
        .registerModule(JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

    @BeforeEach
    fun setUpEach() = runTest {
        receiptRepository.deleteAll()
        val ticketDto = TicketDto(name = "name", address = "address", birthDate = LocalDate.now())
        stubResponse(
            API_YUCCA_TICKET, jacksonMapper
                .writeValueAsString(ticketDto), 200
        )
    }

    @Test
    @Transactional
    fun `should find all with an empty list`() = runTest {
        val (_, referenceSaved, createdDate) = receiptRepository.save(Receipt())
        val findAll = receiptReactiveClient.findAll()
        findAll.shouldNotBeNull()
        findAll.subscribe {
            it.reference shouldBe referenceSaved
            NANOS.between(it.createdAt, createdDate) shouldBeLessThan 1000
        }
        val awaitFirstReceiptDto = findAll.awaitFirst()
        awaitFirstReceiptDto.reference shouldBe referenceSaved
        NANOS.between(awaitFirstReceiptDto.createdAt, createdDate) shouldBeLessThan 1000

        val testTicketDto = TicketDto(
            name = "name",
            address = "address",
            birthDate = LocalDate.now()
        )

        val add = receiptReactiveClient.add(testTicketDto)
        val blockingGet = withContext(Dispatchers.IO) {
            add.blockingGet()
        }
        blockingGet["second"].shouldBe("Saved successfully !")
        val findAll2 = receiptReactiveClient.findAll()
        findAll2.shouldNotBeNull()
        findAll2.subscribe()
        val awaitFirstReceiptDto2 = withContext(Dispatchers.IO) {
            findAll2.toIterable()
        }.toList()
        awaitFirstReceiptDto2.shouldHaveSize(2)
    }

    companion object {
        private val wireMockServer = WireMockServer(WireMockConfiguration().port(7999))

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
    }

    private fun stubResponse(url: String, responseBody: String, responseStatus: Int = HttpStatus.OK.code) {
        wireMockServer.stubFor(
            WireMock.post(url)
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(responseStatus)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .withBody(responseBody)
                )
        )
    }
}