package org.jesperancinha.concert.buy.oyc.concert.client

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import org.jesperancinha.concert.buy.oyc.commons.dto.ConcertDayDto
import org.jesperancinha.concert.buy.oyc.commons.dto.ReceiptDto
import org.jesperancinha.concert.buy.oyc.commons.dto.TicketDto
import reactor.core.publisher.Flux


/**
 * Created by jofisaes on 21/04/2022
 */
@Client("/api")
interface ConcertReactiveClient {
    @Post
    fun add(@Body ticket: ConcertDayDto): Single<LinkedHashMap<String, String>>

    @Get(value = "/", produces = [MediaType.APPLICATION_JSON_STREAM])
    fun findAll(): Flux<ConcertDayDto>
}