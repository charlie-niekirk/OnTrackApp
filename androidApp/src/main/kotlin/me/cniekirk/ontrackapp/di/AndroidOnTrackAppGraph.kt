package me.cniekirk.ontrackapp.di

import android.app.Application
import android.content.Context
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.android.MetroAppComponentProviders
import dev.zacsweers.metrox.viewmodel.ViewModelGraph
import me.cniekirk.ontrackapp.core.common.di.ApplicationContext
import me.cniekirk.ontrackapp.core.database.di.AndroidDatabaseProviders
import me.cniekirk.ontrackapp.core.datastore.di.AndroidDatastoreProviders
import me.cniekirk.ontrackapp.core.network.di.AndroidNetworkProviders

@DependencyGraph(
    scope = AppScope::class,
    bindingContainers = [
        AndroidNetworkProviders::class,
        AndroidDatabaseProviders::class,
        AndroidDatastoreProviders::class
    ]
)
interface AndroidOnTrackAppGraph : MetroAppComponentProviders, ViewModelGraph {

    @Provides
    @ApplicationContext
    fun provideApplicationContext(application: Application): Context = application

    @DependencyGraph.Factory
    interface Factory {
        fun create(@Provides application: Application): AndroidOnTrackAppGraph
    }
}
