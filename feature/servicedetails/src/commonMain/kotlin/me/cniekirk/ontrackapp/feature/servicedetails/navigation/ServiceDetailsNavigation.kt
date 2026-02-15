package me.cniekirk.ontrackapp.feature.servicedetails.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel
import kotlinx.serialization.Serializable
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceDetailRequest
import me.cniekirk.ontrackapp.feature.servicedetails.ServiceDetailsRoute
import me.cniekirk.ontrackapp.feature.servicedetails.ServiceDetailsViewModel

@Serializable
data class ServiceDetails(
    val serviceDetailRequest: ServiceDetailRequest
) : NavKey

fun EntryProviderScope<NavKey>.serviceDetails() {
    entry<ServiceDetails> { serviceDetails ->
        val viewModel = assistedMetroViewModel<ServiceDetailsViewModel, ServiceDetailsViewModel.Factory> {
            create(serviceDetails.serviceDetailRequest)
        }

        ServiceDetailsRoute(viewModel)
    }
}
