package me.cniekirk.ontrackapp.feature.servicelist

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel
import kotlinx.serialization.Serializable
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceDetailRequest
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceListRequest

@Serializable
data class ServiceList(
    val serviceListRequest: ServiceListRequest
) : NavKey

fun EntryProviderScope<NavKey>.serviceList(onServiceDetailRequest: (ServiceDetailRequest) -> Unit) {
    entry<ServiceList> { serviceList ->
        val viewModel = assistedMetroViewModel<ServiceListViewModel, ServiceListViewModel.Factory> {
            create(serviceList.serviceListRequest)
        }

        ServiceListRoute(viewModel) { serviceDetailRequest ->
            onServiceDetailRequest(serviceDetailRequest)
        }
    }
}