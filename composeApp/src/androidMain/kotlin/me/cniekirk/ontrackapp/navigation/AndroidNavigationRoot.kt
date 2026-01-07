package me.cniekirk.ontrackapp.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import me.cniekirk.ontrackapp.core.common.model.StationResult
import me.cniekirk.ontrackapp.core.common.navigation.LocalResultEventBus
import me.cniekirk.ontrackapp.core.common.navigation.ResultEventBus
import me.cniekirk.ontrackapp.feature.home.navigation.Home
import me.cniekirk.ontrackapp.feature.home.navigation.home
import me.cniekirk.ontrackapp.feature.servicelist.ServiceList
import me.cniekirk.ontrackapp.feature.servicelist.serviceList
import me.cniekirk.ontrackapp.feature.stationsearch.navigation.StationSearch
import me.cniekirk.ontrackapp.feature.stationsearch.navigation.stationSearch

@Composable
fun AndroidNavigationRoot(modifier: Modifier = Modifier) {
    val navigationState = rememberNavigationState(
        startRoute = Home,
        topLevelRoutes = TOP_LEVEL_DESTINATIONS.keys
    )
    val navigator = remember { Navigator(navigationState) }
    val resultEventBus = remember { ResultEventBus() }

    BackHandler {
        navigator.goBack()
    }

    MaterialTheme {
        CompositionLocalProvider(LocalResultEventBus provides resultEventBus) {
            Scaffold(
                modifier = modifier,
                bottomBar = {
                    OnTrackNavigationBar(
                        selectedKey = navigationState.topLevelRoute,
                        onSelectKey = { navigator.navigate(it) }
                    )
                }
            ) { innerPadding ->
                NavDisplay(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    onBack = { navigator.goBack() },
                    entries = navigationState.toEntries(
                        entryProvider = entryProvider {
                            home(
                                navigateToStationSelection = { stationType ->
                                    navigator.navigate(StationSearch(stationType))
                                },
                                navigateToServiceList = { serviceListRequest ->
                                    navigator.navigate(ServiceList(serviceListRequest))
                                }
                            )
                            stationSearch { stationResult ->
                                resultEventBus.sendResult<StationResult>(result = stationResult)
                                navigator.goBack()
                            }
                            serviceList { serviceDetailRequest ->
                                // TODO: Navigate to service detail when implemented
                            }
                            entry<Favourites> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Favourites")
                                }
                            }
                            entry<Settings> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Settings")
                                }
                            }
                        }
                    )
                )
            }
        }
    }
}
