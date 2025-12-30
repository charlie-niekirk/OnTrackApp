package me.cniekirk.ontrackapp.feature.home.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.zacsweers.metrox.viewmodel.metroViewModel
import kotlinx.serialization.Serializable
import me.cniekirk.ontrackapp.core.common.model.StationResult
import me.cniekirk.ontrackapp.core.common.model.StationType
import me.cniekirk.ontrackapp.core.common.navigation.ResultEffect
import me.cniekirk.ontrackapp.core.domain.model.Station
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceListRequest
import me.cniekirk.ontrackapp.feature.home.HomeScreen
import me.cniekirk.ontrackapp.feature.home.HomeViewModel

@Serializable
data object Home : NavKey

fun EntryProviderScope<NavKey>.home(
    navigateToStationSelection: (StationType) -> Unit,
    navigateToServiceList: (ServiceListRequest) -> Unit
) {
    entry<Home> {
        val viewModel = metroViewModel<HomeViewModel>()

        ResultEffect<StationResult> { stationResult ->
            viewModel.stationSelected(
                stationType = stationResult.stationType,
                Station(stationResult.crs, stationResult.name)
            )
        }

        HomeScreen(
            viewModel = viewModel,
            navigateToStationSelection = { navigateToStationSelection(it) },
            navigateToServiceList = { navigateToServiceList(it) }
        )
    }
}
