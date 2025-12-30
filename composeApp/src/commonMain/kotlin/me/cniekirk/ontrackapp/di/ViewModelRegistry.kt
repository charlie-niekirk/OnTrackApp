package me.cniekirk.ontrackapp.di

import androidx.lifecycle.ViewModel
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.IntoMap
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import me.cniekirk.ontrackapp.feature.home.HomeViewModel
import me.cniekirk.ontrackapp.feature.servicelist.ServiceListViewModel
import me.cniekirk.ontrackapp.feature.stationsearch.StationSearchViewModel

@BindingContainer
object ViewModelRegistry {

    @Provides
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun bindHomeViewModel(vm: HomeViewModel): ViewModel = vm

    @Provides
    @IntoMap
    @ManualViewModelAssistedFactoryKey(StationSearchViewModel.Factory::class)
    fun bindStationSearchViewModelFactory(factory: StationSearchViewModel.Factory): ManualViewModelAssistedFactory = factory

    @Provides
    @IntoMap
    @ManualViewModelAssistedFactoryKey(ServiceListViewModel.Factory::class)
    fun bindServiceListViewModel(factory: ServiceListViewModel.Factory): ManualViewModelAssistedFactory = factory
}