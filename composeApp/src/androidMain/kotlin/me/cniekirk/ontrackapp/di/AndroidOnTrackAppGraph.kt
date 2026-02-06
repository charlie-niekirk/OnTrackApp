package me.cniekirk.ontrackapp.di

import android.app.Application
import android.content.Context
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import me.cniekirk.ontrackapp.core.common.di.ApplicationContext
import me.cniekirk.ontrackapp.core.data.di.DataProviders
import me.cniekirk.ontrackapp.core.database.di.AndroidDatabaseProviders
import me.cniekirk.ontrackapp.core.network.di.AndroidNetworkProviders

@DependencyGraph(
    scope = AppScope::class,
    bindingContainers = [
        DataProviders::class,
        AndroidNetworkProviders::class,
        AndroidDatabaseProviders::class
    ]
)
interface AndroidOnTrackAppGraph : OnTrackAppGraph {

    @Provides
    @ApplicationContext
    fun provideApplicationContext(application: Application): Context = application

    @DependencyGraph.Factory
    interface Factory {
        fun create(@Provides application: Application): AndroidOnTrackAppGraph
    }
}