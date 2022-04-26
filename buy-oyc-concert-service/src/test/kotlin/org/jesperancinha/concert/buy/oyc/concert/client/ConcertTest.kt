package org.jesperancinha.concert.buy.oyc.concert.client

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.flywaydb.core.Flyway
import org.jesperancinha.concert.buy.oyc.commons.domain.ConcertDay
import org.jesperancinha.concert.buy.oyc.commons.domain.ConcertDayRepository
import org.jesperancinha.concert.buy.oyc.commons.domain.ConcertDayReservationRepository
import org.jesperancinha.concert.buy.oyc.commons.dto.ConcertDayDto
import org.jesperancinha.concert.buy.oyc.containers.AbstractBuyOddYuccaConcertContainerTest
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.Thread.sleep
import java.time.LocalDate
import java.util.*
import javax.transaction.Transactional

/**
 * Created by jofisaes on 21/04/2022
 */
@ExperimentalCoroutinesApi
@MicronautTest
class ReceiptTest @Inject constructor(
    private val concertRepository: ConcertDayRepository,
    private val concertDayReservationRepository: ConcertDayReservationRepository,
    private val concertReactiveClient: ConcertReactiveClient,
) : AbstractBuyOddYuccaConcertContainerTest() {

    @BeforeEach
    fun setUpEach() = runTest {
        concertRepository.deleteAll()
        concertDayReservationRepository.deleteAll()
    }

    @Test
    @Transactional
    fun `should create concert day reservation`() = runTest {
        val (id, _, _, _, _) = concertRepository.save(
            ConcertDay(
                name = "The Sweet Potato Tour",
                description = "A celebration of all potatoes of the World",
                concertDate = LocalDate.now()
            )
        )
        val findAll = concertReactiveClient.findAll()
        findAll.shouldNotBeNull()
        findAll.subscribe()

        val awaitFirstReceiptDto = findAll.awaitFirstOrNull()
        awaitFirstReceiptDto.shouldBeNull()

        val concertDayDto = ConcertDayDto(
            reference = UUID.randomUUID(),
            concertId = id
        )

        val add = concertReactiveClient.add(concertDayDto)
        val blockingGet = withContext(Dispatchers.IO) {
            add.blockingGet()
        }
        blockingGet.message.shouldBe("Saved successfully !")
        val findAll2 = concertReactiveClient.findAll()
        findAll2.shouldNotBeNull()
        findAll2.subscribe()
        withContext(Dispatchers.IO) {
            sleep(1000)
        }
        val awaitFirstReceiptDto2 = withContext(Dispatchers.IO) {
            findAll2.toIterable()
        }.toList()
        awaitFirstReceiptDto2.shouldHaveSize(1)
    }

    companion object {
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
}