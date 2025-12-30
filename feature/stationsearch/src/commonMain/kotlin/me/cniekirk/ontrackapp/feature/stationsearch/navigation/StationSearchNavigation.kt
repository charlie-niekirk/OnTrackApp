package me.cniekirk.ontrackapp.feature.stationsearch.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel
import kotlinx.serialization.Serializable
import me.cniekirk.ontrackapp.core.common.model.StationResult
import me.cniekirk.ontrackapp.core.common.model.StationType
import me.cniekirk.ontrackapp.feature.stationsearch.StationSearchScreen
import me.cniekirk.ontrackapp.feature.stationsearch.StationSearchViewModel

@Serializable
data class StationSearch(
    val stationType: StationType
) : NavKey

fun EntryProviderScope<NavKey>.stationSearch(onStationResult: (StationResult) -> Unit) {
    entry<StationSearch> { stationSearch ->
        val viewModel = assistedMetroViewModel<StationSearchViewModel, StationSearchViewModel.Factory> {
            create(stationSearch.stationType)
        }

        StationSearchScreen(viewModel) { stationResult ->
            onStationResult(stationResult)
        }
    }
}
