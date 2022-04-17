package org.jesperancinha.concert.buy.oyc.commons.dto

import org.jesperancinha.concert.buy.oyc.commons.domain.AuditLogType
import org.jesperancinha.concert.buy.oyc.commons.domain.AuditLogType.DRINK
import org.jesperancinha.concert.buy.oyc.commons.domain.AuditLogType.MEAL
import org.jesperancinha.concert.buy.oyc.commons.domain.BuyOycType
import java.io.Serializable
import java.util.*

/**
 * Created by jofisaes on 17/04/2022
 */
data class MealDto(
    val reference: UUID,
    val drink: UUID,
    override val type: AuditLogType = MEAL
) : Serializable, BuyOycType

data class DrinkDto(
    val reference: UUID,
    val drink: UUID,
    override val type: AuditLogType = DRINK
) : Serializable, BuyOycType
