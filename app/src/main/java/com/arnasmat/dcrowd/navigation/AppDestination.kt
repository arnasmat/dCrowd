package com.arnasmat.dcrowd.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.math.BigInteger

/**
 * Custom serializer for BigInteger since it's not serializable by default
 */
object BigIntegerSerializer : KSerializer<BigInteger> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("BigInteger", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: BigInteger) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): BigInteger {
        return BigInteger(decoder.decodeString())
    }
}

@Serializable
data object ProjectList : NavKey

@Serializable
data object CreateProject : NavKey

@Serializable
data class ProjectDetail(
    @Serializable(with = BigIntegerSerializer::class)
    val projectIdx: BigInteger
) : NavKey

@Serializable
data object UserSelector : NavKey

@Serializable
data object Web3Setup : NavKey

