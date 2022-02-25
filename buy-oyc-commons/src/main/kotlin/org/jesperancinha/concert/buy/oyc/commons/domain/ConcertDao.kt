package org.jesperancinha.concert.buy.oyc.commons.domain

import io.micronaut.data.annotation.AutoPopulated
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.model.naming.NamingStrategies
import java.time.LocalDate
import java.util.*

/**
 * Created by jofisaes on 25/02/2022
 */
@MappedEntity(value = "concert_day", namingStrategy = NamingStrategies.UnderScoreSeparatedLowerCase::class)
data class ConcertDay(
    @field: Id
    @field: AutoPopulated
    val id: UUID? = null,
    val name: String,
    val description:String,
    val data: LocalDate
)