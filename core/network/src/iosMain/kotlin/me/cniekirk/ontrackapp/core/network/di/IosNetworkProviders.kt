package me.cniekirk.ontrackapp.core.network.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import me.cniekirk.ontrackapp.core.network.HttpClientFactory
import me.cniekirk.ontrackapp.core.network.ORD_BASE_URL
import me.cniekirk.ontrackapp.core.network.RTT_BASE_URL
import me.cniekirk.ontrackapp.core.network.RTT_PASSWORD
import me.cniekirk.ontrackapp.core.network.RTT_USERNAME

@BindingContainer(includes = [CommonNetworkProviders::class])
object IosNetworkProviders {

    @Named("ord-client")
    @Provides
    @SingleIn(AppScope::class)
    fun provideOpenRailDataHttpClient(): HttpClient {
        return HttpClientFactory.create(
            engine = Darwin.create(),
            baseUrl = ORD_BASE_URL,
        )
    }

    @Named("realtime-trains-client")
    @Provides
    @SingleIn(AppScope::class)
    fun provideRealTimeTrainsHttpClient(): HttpClient {
        return HttpClientFactory.create(
            engine = Darwin.create(),
            baseUrl = RTT_BASE_URL,
        ) {
            install(Auth) {
                basic {
                    credentials {
                        BasicAuthCredentials(
                            username = RTT_USERNAME,
                            password = RTT_PASSWORD
                        )
                    }
                    sendWithoutRequest { true }
                }
            }
        }
    }
}