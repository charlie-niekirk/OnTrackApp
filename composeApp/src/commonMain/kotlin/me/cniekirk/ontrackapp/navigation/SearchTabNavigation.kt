package me.cniekirk.ontrackapp.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
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
fun SearchTabNavigation(modifier: Modifier = Modifier) {
    val config = SavedStateConfiguration {
        serializersModule = SerializersModule {
            polymorphic(NavKey::class) {
                subclass(Home::class, Home.serializer())
            }
        }
    }

    val backStack = rememberNavBackStack(config, Home)
    val resultEventBus = remember { ResultEventBus() }

    CompositionLocalProvider(LocalResultEventBus.provides(resultEventBus)) {
        NavDisplay(
            modifier = modifier.fillMaxSize(),
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider {
                home(
                    navigateToStationSelection = { stationType ->
                        backStack.add(StationSearch(stationType))
                    },
                    navigateToServiceList = { serviceListRequest ->
                        backStack.add(ServiceList(serviceListRequest))
                    }
                )
                stationSearch { stationResult ->
                    resultEventBus.sendResult<StationResult>(result = stationResult)
                    backStack.removeLastOrNull()
                }
                serviceList {
                }
            }
        )
    }
}
