package org.jesperancinha.concert.buy.oyc.api.client

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import org.jesperancinha.concert.buy.oyc.api.dto.TicketDto


/**
 * Created by jofisaes on 09/04/2022
 */
@Client("/api/tickets")
interface TicketReactiveClient {
    @Post
    fun add(@Body ticket: TicketDto?): Single<TicketDto>

    @Get("/{id}")
    fun findById(@PathVariable id: Int?): Maybe<TicketDto>

    @Get(value = "/stream", produces = [MediaType.APPLICATION_JSON_STREAM])
    fun findAllStream(): Flowable<TicketDto>
}