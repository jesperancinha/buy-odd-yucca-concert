package org.jesperancinha.concert.buy.oyc.commons.rest

import io.micronaut.http.HttpHeaders.ACCEPT
import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType.APPLICATION_JSON
import io.micronaut.rxjava3.http.client.Rx3HttpClient
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.internal.schedulers.SingleScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jesperancinha.concert.buy.oyc.commons.domain.AuditLog
import org.jesperancinha.concert.buy.oyc.commons.domain.AuditLogRepository
import org.jesperancinha.concert.buy.oyc.commons.domain.BuyOycType
import org.jesperancinha.concert.buy.oyc.commons.dto.ResponseDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val logger: Logger = LoggerFactory.getLogger(BuyOycType::class.java)

/**
 * Created by jofisaes on 15/04/2022
 */
inline fun <reified T : BuyOycType> Rx3HttpClient.sendObject(
    buyOycType: T,
    url: String,
    auditLogRepository: AuditLogRepository
): Single<ResponseDto> =
    retrieve(HttpRequest.POST(url, buyOycType).header(ACCEPT, APPLICATION_JSON), ResponseDto::class.java)
        .firstOrError()
        .doOnSuccess {
            CoroutineScope(Dispatchers.IO).launch {
                auditLogRepository.save(
                    AuditLog(
                        auditLogType = buyOycType.type,
                        payload = buyOycType.toString()
                    )
                )
            }
        }
        .doOnError {
            logger.error("ERROR", it)
        }
