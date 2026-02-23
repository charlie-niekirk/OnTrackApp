package me.cniekirk.ontrackapp.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import me.cniekirk.ontrackapp.feature.pinned.navigation.Pinned
import me.cniekirk.ontrackapp.feature.pinned.navigation.pinned
import me.cniekirk.ontrackapp.feature.servicedetails.navigation.ServiceDetails
import me.cniekirk.ontrackapp.feature.servicedetails.navigation.serviceDetails

@Composable
fun PinnedTabNavigation(modifier: Modifier = Modifier) {
    val config = SavedStateConfiguration {
        serializersModule = SerializersModule {
            polymorphic(NavKey::class) {
                subclass(Pinned::class, Pinned.serializer())
                subclass(ServiceDetails::class, ServiceDetails.serializer())
            }
        }
    }

    val backStack = rememberNavBackStack(config, Pinned)

    NavDisplay(
        modifier = modifier.fillMaxSize(),
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            pinned { pinnedService ->
                backStack.add(
                    ServiceDetails(
                        serviceDetailRequest = pinnedService.serviceDetailRequest,
                        targetStation = pinnedService.targetStation,
                        filterStation = pinnedService.filterStation
                    )
                )
            }
            serviceDetails()
        }
    )
}
