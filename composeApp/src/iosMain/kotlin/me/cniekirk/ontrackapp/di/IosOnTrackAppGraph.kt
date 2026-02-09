package me.cniekirk.ontrackapp.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import me.cniekirk.ontrackapp.core.database.di.IosDatabaseProviders
import me.cniekirk.ontrackapp.core.datastore.di.IosDatastoreProviders
import me.cniekirk.ontrackapp.core.network.di.IosNetworkProviders

@DependencyGraph(
    scope = AppScope::class,
    bindingContainers = [
        IosDatabaseProviders::class,
        IosNetworkProviders::class,
        IosDatastoreProviders::class
    ]
)
abstract class IosOnTrackAppGraph : OnTrackAppGraph