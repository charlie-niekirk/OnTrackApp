package me.cniekirk.ontrackapp.di

import androidx.lifecycle.ViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provider
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactory
import me.cniekirk.ontrackapp.core.data.di.DataProviders
import me.cniekirk.ontrackapp.core.database.di.IosDatabaseProviders
import me.cniekirk.ontrackapp.core.network.di.IosNetworkProviders
import kotlin.reflect.KClass

@DependencyGraph(
    scope = AppScope::class,
    bindingContainers = [
        DataProviders::class,
        IosDatabaseProviders::class,
        IosNetworkProviders::class,
        ViewModelRegistry::class
    ]
)
abstract class IosOnTrackAppGraph : OnTrackAppGraph {

    @Provides
    @SingleIn(AppScope::class)
    fun bindMetroViewModelFactory(
        viewModelProviders: Map<KClass<out ViewModel>, Provider<ViewModel>>,
        assistedFactoryProviders: Map<KClass<out ViewModel>, Provider<ViewModelAssistedFactory>>,
        manualAssistedFactoryProviders: Map<KClass<out ManualViewModelAssistedFactory>, Provider<ManualViewModelAssistedFactory>>
    ): MetroViewModelFactory = IosViewModelFactory(
        viewModelProviders,
        assistedFactoryProviders,
        manualAssistedFactoryProviders
    )
}