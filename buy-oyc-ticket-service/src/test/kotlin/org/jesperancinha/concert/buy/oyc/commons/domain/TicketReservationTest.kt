@file:OptIn(ExperimentalCoroutinesApi::class)

package org.jesperancinha.concert.buy.oyc.commons.domain

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

/**
 * Created by jofisaes on 25/02/2022
 */
@MicronautTest
@Disabled
class TicketReservationTest(
    val ticketRepository: TicketRepository
) {
    @Test
    fun `should read an empty ticket list from repository`() = runTest {
        ticketRepository.findAll().toList()
    }
}