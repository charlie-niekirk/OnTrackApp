package me.cniekirk.ontrackapp.feature.stationsearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import me.cniekirk.ontrackapp.core.common.model.StationType
import me.cniekirk.ontrackapp.core.domain.model.Station
import me.cniekirk.ontrackapp.core.domain.repository.StationsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

@OptIn(FlowPreview::class)
@AssistedInject
class StationSearchViewModel(
    private val stationsRepository: StationsRepository,
    @Assisted private val stationType: StationType
) : ViewModel(), ContainerHost<StationSearchState, StationSearchEffect> {

    private var stationList: List<Station> = emptyList()
    private val searchQueryFlow = MutableStateFlow("")

    override val container = container<StationSearchState, StationSearchEffect>(StationSearchState(stationType)) {
        viewModelScope.launch {
            searchQueryFlow.debounce(300).collect { query ->
                filterStations(query)
            }
        }
        fetchStations()
    }

    fun searchStations(query: String) = intent {
        reduce { state.copy(searchQuery = query) }
        searchQueryFlow.value = query
    }

    private fun filterStations(query: String) = intent {
        val filtered = stationList.filter {
            it.crs.contains(query, ignoreCase = true) || it.name.contains(query, ignoreCase = true)
        }
        reduce { state.copy(stations = filtered) }
    }

    private fun fetchStations() = intent {
        stationsRepository.getStations()
            .onSuccess { stations ->
                stationList = stations
                reduce {
                    state.copy(
                        isLoading = false,
                        stations = stations
                    )
                }
            }
            .onFailure { throwable ->
                Logger.e("Error: ${throwable.message}")
                reduce {
                    state.copy(isLoading = false)
                }
            }
    }

    @AssistedFactory
    @ManualViewModelAssistedFactoryKey(Factory::class)
    @ContributesIntoMap(AppScope::class)
    interface Factory : ManualViewModelAssistedFactory {
        fun create(stationType: StationType): StationSearchViewModel
    }
}