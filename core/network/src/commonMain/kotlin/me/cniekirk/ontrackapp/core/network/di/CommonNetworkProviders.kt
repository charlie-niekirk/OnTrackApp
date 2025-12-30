package me.cniekirk.ontrackapp.core.network.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import me.cniekirk.ontrackapp.core.network.service.OpenRailDataService
import me.cniekirk.ontrackapp.core.network.service.OpenRailDataServiceImpl
import me.cniekirk.ontrackapp.core.network.service.RealTimeTrainsService
import me.cniekirk.ontrackapp.core.network.service.RealTimeTrainsServiceImpl

@BindingContainer
object CommonNetworkProviders {

    @Provides
    @SingleIn(AppScope::class)
    fun provideOpenRailDataApiService(@Named("ord-client") httpClient: HttpClient): OpenRailDataService {
        return OpenRailDataServiceImpl(httpClient)
    }

    @Provides
    @SingleIn(AppScope::class)
    fun provideRealTimeTrainsService(@Named("realtime-trains-client") httpClient: HttpClient): RealTimeTrainsService {
        return RealTimeTrainsServiceImpl(httpClient)
    }
}