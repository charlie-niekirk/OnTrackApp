package me.cniekirk.ontrackapp.core.datastore.serializer

import androidx.datastore.core.okio.OkioSerializer
import kotlinx.serialization.json.Json
import me.cniekirk.ontrackapp.core.datastore.model.PinnedServices
import okio.BufferedSink
import okio.BufferedSource

internal object PinnedServicesSerializer : OkioSerializer<PinnedServices> {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    override val defaultValue: PinnedServices = PinnedServices()

    override suspend fun readFrom(source: BufferedSource): PinnedServices {
        return try {
            json.decodeFromString(
                PinnedServices.serializer(),
                source.readUtf8()
            )
        } catch (e: Exception) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: PinnedServices, sink: BufferedSink) {
        sink.writeUtf8(
            json.encodeToString(PinnedServices.serializer(), t)
        )
    }
}
