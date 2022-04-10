package org.jesperancinha.concert.buy.oyc.api.client

import io.kotest.matchers.nulls.shouldNotBeNull
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.flywaydb.core.Flyway
import org.jesperancinha.concert.buy.oyc.api.containers.AbstractBuyOddYuccaConcertContainerTest
import org.jesperancinha.concert.buy.oyc.commons.domain.Receipt
import org.jesperancinha.concert.buy.oyc.commons.domain.ReceiptRepository
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


/**
 * Created by jofisaes on 10/04/2022
 */
@ExperimentalCoroutinesApi
@MicronautTest
class ReceiptTest @Inject constructor(
    private val receiptRepository: ReceiptRepository,
    private val receiptReactiveClient: ReceiptReactiveClient
) : AbstractBuyOddYuccaConcertContainerTest() {

    @BeforeEach
    fun setUpEach(){
        runBlocking {
            receiptRepository.deleteAll()
        }
    }

    @Test
    fun `should find all with an empty list`() {
//        runBlocking {
//            receiptRepository.save(Receipt())
//        }
        val findById = receiptReactiveClient.findAll()

        findById.shouldNotBeNull()
        findById.subscribe {
            print(it)
        }
    }
    companion object {
        @JvmStatic
        @BeforeAll
        fun setUp() {
            postgreSQLContainer.start()
            redis.start()
            config.setDataSource(
                postgreSQLContainer.jdbcUrl,
                postgreSQLContainer.username,
                postgreSQLContainer.password
            )
            config.schemas = arrayOf("ticket")
            Flyway(config).migrate()
        }
    }
}