package me.cniekirk.ontrackapp.core.datastore.serializer

import androidx.datastore.core.okio.OkioSerializer
import kotlinx.serialization.json.Json
import me.cniekirk.ontrackapp.core.datastore.model.ThemePreferences
import okio.BufferedSink
import okio.BufferedSource

internal object ThemePreferencesSerializer : OkioSerializer<ThemePreferences> {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    override val defaultValue: ThemePreferences = ThemePreferences()

    override suspend fun readFrom(source: BufferedSource): ThemePreferences {
        return try {
            json.decodeFromString(
                ThemePreferences.serializer(),
                source.readUtf8()
            )
        } catch (e: Exception) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: ThemePreferences, sink: BufferedSink) {
        sink.writeUtf8(
            json.encodeToString(ThemePreferences.serializer(), t)
        )
    }
}
