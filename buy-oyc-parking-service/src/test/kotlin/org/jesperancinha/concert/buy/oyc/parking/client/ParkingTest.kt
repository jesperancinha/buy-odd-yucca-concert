package org.jesperancinha.concert.buy.oyc.parking.client

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
import org.jesperancinha.concert.buy.oyc.commons.domain.CarParking
import org.jesperancinha.concert.buy.oyc.commons.domain.CarParkingRepository
import org.jesperancinha.concert.buy.oyc.commons.domain.ConcertDayReservationRepository
import org.jesperancinha.concert.buy.oyc.commons.dto.ParkingReservationDto
import org.jesperancinha.concert.buy.oyc.containers.AbstractBuyOddYuccaConcertContainerTest
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.Thread.sleep
import java.util.*
import javax.transaction.Transactional

/**
 * Created by jofisaes on 21/04/2022
 */
@ExperimentalCoroutinesApi
@MicronautTest
class ParkingTest @Inject constructor(
    private val parkingRepository: CarParkingRepository,
    private val parkingReservationRepository: ConcertDayReservationRepository,
    private val parkingReactiveClient: ParkingReactiveClient,
) : AbstractBuyOddYuccaConcertContainerTest() {

    @BeforeEach
    fun setUpEach() = runTest {
        parkingRepository.deleteAll()
        parkingReservationRepository.deleteAll()
    }

    @Test
    @Transactional
    fun `should create car parking reservation`() = runTest {
        val (_, parkingNumber, _) = parkingRepository.save(
            CarParking(
                parkingNumber = 1
            )
        )
        val findAll = parkingReactiveClient.findAll()
        findAll.shouldNotBeNull()
        findAll.subscribe()

        val awaitFirstReceiptDto = findAll.awaitFirstOrNull()
        awaitFirstReceiptDto.shouldBeNull()

        val concertDayDto = ParkingReservationDto(
            reference = UUID.randomUUID(),
            carParkingId = parkingNumber
        )

        val add = parkingReactiveClient.add(concertDayDto)
        val blockingGet = withContext(Dispatchers.IO) {
            add.blockingGet()
        }
        blockingGet.message.shouldBe("Saved successfully !")
        val findAll2 = parkingReactiveClient.findAll()
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
            config.isCleanDisabled = false
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