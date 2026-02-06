package me.cniekirk.ontrackapp.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import me.cniekirk.ontrackapp.core.data.di.DataProviders
import me.cniekirk.ontrackapp.core.database.di.IosDatabaseProviders
import me.cniekirk.ontrackapp.core.network.di.IosNetworkProviders

@DependencyGraph(
    scope = AppScope::class,
    bindingContainers = [
        DataProviders::class,
        IosDatabaseProviders::class,
        IosNetworkProviders::class
    ]
)
abstract class IosOnTrackAppGraph : OnTrackAppGraph