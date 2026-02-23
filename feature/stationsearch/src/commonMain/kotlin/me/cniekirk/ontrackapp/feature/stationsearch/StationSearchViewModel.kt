package me.cniekirk.ontrackapp.feature.stationsearch

import androidx.lifecycle.ViewModel
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
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

@AssistedInject
class StationSearchViewModel(
    private val stationsRepository: StationsRepository,
    @Assisted private val stationType: StationType
) : ViewModel(), ContainerHost<StationSearchState, StationSearchEffect> {

    private var stationList: List<Station> = emptyList()

    override val container = container<StationSearchState, StationSearchEffect>(
        StationSearchState.Loading(stationType = stationType)
    ) {
        fetchStations()
    }

    fun searchStations(query: String) = intent {
        val currentState = state as? StationSearchState.Ready ?: return@intent
        val updatedState = currentState.copy(searchQuery = query)
        reduce { updatedState }

        val filteredStations = stationList.filter { station ->
            station.crs.contains(query, ignoreCase = true) || station.name.contains(query, ignoreCase = true)
        }

        reduce {
            updatedState.copy(
                stations = filteredStations
            )
        }
    }

    private fun fetchStations() = intent {
        stationsRepository.getStations()
            .onSuccess { stations ->
                stationList = stations
                reduce {
                    StationSearchState.Ready(
                        stationType = state.stationType,
                        stations = stations
                    )
                }
            }
            .onFailure { throwable ->
                Logger.e("Error: ${throwable.message}")
                reduce {
                    StationSearchState.Error(
                        stationType = state.stationType,
                        errorType = throwable.message ?: "Unknown error"
                    )
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
