package org.jesperancinha.concert.buy.oyc.api.client

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import org.jesperancinha.concert.buy.oyc.commons.dto.ReceiptDto
import org.jesperancinha.concert.buy.oyc.commons.dto.ResponseDto
import org.jesperancinha.concert.buy.oyc.commons.dto.TicketDto
import reactor.core.publisher.Flux


/**
 * Created by jofisaes on 09/04/2022
 */
@Client("/api")
interface ReceiptReactiveClient {
    @Post
    fun add(@Body ticket: TicketDto): Single<ResponseDto>

    @Get("/{id}")
    fun findById(@PathVariable id: Int?): Maybe<ReceiptDto>

    @Get(value = "/", produces = [MediaType.APPLICATION_JSON_STREAM])
    fun findAll(): Flux<ReceiptDto>
}