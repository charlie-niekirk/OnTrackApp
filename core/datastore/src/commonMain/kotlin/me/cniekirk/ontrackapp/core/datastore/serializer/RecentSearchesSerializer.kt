package me.cniekirk.ontrackapp.core.datastore.serializer

import androidx.datastore.core.okio.OkioSerializer
import kotlinx.serialization.json.Json
import me.cniekirk.ontrackapp.core.datastore.model.RecentSearches
import okio.BufferedSink
import okio.BufferedSource

internal object RecentSearchesSerializer : OkioSerializer<RecentSearches> {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    override val defaultValue: RecentSearches = RecentSearches()

    override suspend fun readFrom(source: BufferedSource): RecentSearches {
        return try {
            json.decodeFromString(
                RecentSearches.serializer(),
                source.readUtf8()
            )
        } catch (e: Exception) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: RecentSearches, sink: BufferedSink) {
        sink.writeUtf8(
            json.encodeToString(RecentSearches.serializer(), t)
        )
    }
}
