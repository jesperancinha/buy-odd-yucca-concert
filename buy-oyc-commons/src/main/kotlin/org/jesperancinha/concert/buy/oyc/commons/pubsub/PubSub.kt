package org.jesperancinha.concert.buy.oyc.commons.pubsub

import io.lettuce.core.RedisClient
import io.lettuce.core.codec.RedisCodec
import io.lettuce.core.pubsub.RedisPubSubAdapter

/**
 * Created by jofisaes on 13/04/2022
 */

fun <V> RedisClient.initPubSub(
    channelName: String,
    redisCodec: RedisCodec<String, V>,
    redisPubSubAdapter: RedisPubSubAdapter<String, V>
) {
    val statefulRedisPubSubConnection = connectPubSub(redisCodec)
    statefulRedisPubSubConnection.addListener(redisPubSubAdapter)
    val redisPubSubAsyncCommands = statefulRedisPubSubConnection.async()
    redisPubSubAsyncCommands.subscribe(channelName)
}