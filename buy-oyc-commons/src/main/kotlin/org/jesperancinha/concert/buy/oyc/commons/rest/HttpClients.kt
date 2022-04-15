package org.jesperancinha.concert.buy.oyc.commons.rest

import io.micronaut.http.HttpRequest
import io.micronaut.rxjava3.http.client.Rx3StreamingHttpClient
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.internal.schedulers.SingleScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jesperancinha.concert.buy.oyc.commons.domain.AuditLog
import org.jesperancinha.concert.buy.oyc.commons.domain.AuditLogRepository
import org.jesperancinha.concert.buy.oyc.commons.domain.BuyOycType

/**
 * Created by jofisaes on 15/04/2022
 */
inline fun <reified T : BuyOycType> Rx3StreamingHttpClient.sendObject(
    buyOycType: T,
    url: String,
    auditLogRepository: AuditLogRepository
) {
    val ticketDtoSingle: Single<T> =
        retrieve(HttpRequest.POST(url, buyOycType), T::class.java).firstOrError()
    val singleScheduler = SingleScheduler()
    ticketDtoSingle.subscribeOn(singleScheduler).doOnSuccess {
        CoroutineScope(Dispatchers.IO).launch {
            auditLogRepository.save(
                AuditLog(
                    auditLogType = buyOycType.type,
                    payload = buyOycType.toString()
                )
            )
        }

    }.subscribe()
    singleScheduler.start()
}