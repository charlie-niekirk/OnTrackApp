package me.cniekirk.ontrackapp.core.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal object HttpClientFactory {

    fun create(
        engine: HttpClientEngine,
        baseUrl: String,
        block: HttpClientConfig<*>.() -> Unit = {}
    ): HttpClient {
        return HttpClient(engine = engine) {
            // JSON serialization
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }

            // Default request configuration
            if (baseUrl.isNotEmpty()) {
                defaultRequest {
                    url(baseUrl)
                }
            }

            block()
        }
    }
}
