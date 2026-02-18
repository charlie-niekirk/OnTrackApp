package me.cniekirk.ontrackapp.feature.pinned.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.zacsweers.metrox.viewmodel.metroViewModel
import kotlinx.serialization.Serializable
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceDetailRequest
import me.cniekirk.ontrackapp.feature.pinned.PinnedRoute
import me.cniekirk.ontrackapp.feature.pinned.PinnedViewModel

@Serializable
data object Pinned : NavKey

fun EntryProviderScope<NavKey>.pinned(onServiceSelected: (ServiceDetailRequest) -> Unit) {
    entry<Pinned> {
        val viewModel = metroViewModel<PinnedViewModel>()

        PinnedRoute(
            viewModel = viewModel,
            onServiceSelected = onServiceSelected
        )
    }
}
