package org.jesperancinha.concert.buy.oyc.ticket.client

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client
import io.reactivex.rxjava3.core.Single
import org.jesperancinha.concert.buy.oyc.commons.dto.TicketDto
import reactor.core.publisher.Flux


/**
 * Created by jofisaes on 09/04/2022
 */
@Client("/api")
interface TicketReactiveClient {
    @Post
    fun add(@Body ticket: TicketDto): Single<LinkedHashMap<String, String>>

    @Get(value = "/", produces = [MediaType.APPLICATION_JSON_STREAM])
    fun getAllTickets(): Flux<TicketDto>
}