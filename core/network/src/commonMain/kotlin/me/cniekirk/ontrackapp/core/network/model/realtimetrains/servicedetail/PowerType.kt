package me.cniekirk.ontrackapp.core.network.model.realtimetrains.servicedetail

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive

@Serializable(with = PowerTypeSerializer::class)
enum class PowerType(
    val apiValue: String,
    val description: String
) {
    DIESEL("D", "Diesel"),
    DIESEL_ELECTRIC_MULTIPLE_UNIT("DEM", "Diesel Electric Multiple Unit"),
    DIESEL_MECHANICAL_MULTIPLE_UNIT("DMU", "Diesel Mechanical Multiple Unit"),
    ELECTRIC("E", "Electric"),
    ELECTRO_DIESEL("ED", "Electro-Diesel"),
    EMU_PLUS_D_E_ED_LOCOMOTIVE("EML", "EMU plus D, E, ED locomotive"),
    ELECTRIC_MULTIPLE_UNIT("EMU", "Electric Multiple Unit"),
    HIGH_SPEED_TRAIN("HST", "High Speed Train"),
    UNKNOWN("UNKNOWN", "Unknown");

    companion object {
        fun fromApiValue(value: String?): PowerType {
            val normalizedValue = value?.trim()?.uppercase() ?: return UNKNOWN
            return entries.firstOrNull { it.apiValue == normalizedValue } ?: UNKNOWN
        }
    }
}

object PowerTypeSerializer : KSerializer<PowerType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("PowerType", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): PowerType {
        return if (decoder is JsonDecoder) {
            when (val element = decoder.decodeJsonElement()) {
                JsonNull -> PowerType.UNKNOWN
                is JsonPrimitive -> {
                    val value = if (element.isString) element.content else null
                    PowerType.fromApiValue(value)
                }
                else -> PowerType.UNKNOWN
            }
        } else {
            PowerType.fromApiValue(runCatching { decoder.decodeString() }.getOrNull())
        }
    }

    override fun serialize(encoder: Encoder, value: PowerType) {
        encoder.encodeString(value.apiValue)
    }
}
