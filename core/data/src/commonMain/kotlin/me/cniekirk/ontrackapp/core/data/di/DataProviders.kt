package me.cniekirk.ontrackapp.core.data.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import me.cniekirk.ontrackapp.core.data.repository.RealTimeTrainsRepositoryImpl
import me.cniekirk.ontrackapp.core.data.repository.StationsRepositoryImpl
import me.cniekirk.ontrackapp.core.database.StationDatabase
import me.cniekirk.ontrackapp.core.domain.repository.RealtimeTrainsRepository
import me.cniekirk.ontrackapp.core.domain.repository.StationsRepository
import me.cniekirk.ontrackapp.core.network.service.OpenRailDataService
import me.cniekirk.ontrackapp.core.network.service.RealTimeTrainsService

@BindingContainer
object DataProviders {

    @Provides
    @SingleIn(AppScope::class)
    fun provideStationsRepository(
        openRailDataService: OpenRailDataService,
        stationDatabase: StationDatabase
    ): StationsRepository = StationsRepositoryImpl(openRailDataService, stationDatabase)

    @Provides
    @SingleIn(AppScope::class)
    fun provideRealTimeTrainsRepository(
        realTimeTrainsService: RealTimeTrainsService
    ) : RealtimeTrainsRepository = RealTimeTrainsRepositoryImpl(realTimeTrainsService)
}