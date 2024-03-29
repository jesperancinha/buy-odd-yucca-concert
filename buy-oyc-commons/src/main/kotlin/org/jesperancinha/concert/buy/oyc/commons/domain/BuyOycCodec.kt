package org.jesperancinha.concert.buy.oyc.commons.domain

import io.lettuce.core.codec.ByteArrayCodec
import io.lettuce.core.codec.RedisCodec
import org.jesperancinha.concert.buy.oyc.commons.domain.AuditLogType.RECEIPT
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.nio.ByteBuffer
import java.nio.charset.Charset

abstract class BuyOycType(
    open val type: AuditLogType = RECEIPT
) : Serializable

abstract class BuyOycCodec<T> : RedisCodec<String, T> {

    override fun decodeKey(byteBuffer: ByteBuffer): String {
        return Charset.defaultCharset().decode(byteBuffer).toString()
    }

    override fun decodeValue(byteBuffer: ByteBuffer): T =
        ObjectInputStream(
            ByteArrayInputStream(byteArrayCodec.decodeValue(byteBuffer))
        ).use { readCodecObject(it) }

    abstract fun readCodecObject(it: ObjectInputStream): T

    override fun encodeKey(key: String): ByteBuffer {
        return Charset.defaultCharset().encode(key)
    }

    override fun encodeValue(ticketDto: T): ByteBuffer =
        ByteArrayOutputStream().use { baos ->
            ObjectOutputStream(baos).use { oos ->
                oos.writeObject(ticketDto)
                byteArrayCodec.encodeValue(baos.toByteArray())
            }
        }

    companion object {
        val byteArrayCodec = ByteArrayCodec()
    }
}

inline fun <reified T : Any> ObjectInputStream.readTypedObject(): T = readObject() as T
