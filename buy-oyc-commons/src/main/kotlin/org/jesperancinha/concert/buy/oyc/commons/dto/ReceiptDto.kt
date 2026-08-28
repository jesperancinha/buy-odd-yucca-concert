package org.jesperancinha.concert.buy.oyc.commons.dto

import org.jesperancinha.concert.buy.oyc.commons.domain.Receipt
import java.time.LocalDateTime
import java.util.*

data class ReceiptDto(
    val reference: UUID,
    val createdAt: LocalDateTime,
)

val Receipt.toDto: ReceiptDto
    get() = ReceiptDto(
        reference = reference ?: throw RuntimeException("No reference found for this Receipt!"),
        createdAt = createdAt ?: throw RuntimeException("This Receipt does not have a created date!")
    )
