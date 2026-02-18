package me.cniekirk.ontrackapp.core.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.cniekirk.ontrackapp.core.datastore.model.PinnedServiceData
import me.cniekirk.ontrackapp.core.datastore.model.PinnedServices
import me.cniekirk.ontrackapp.core.datastore.model.ServiceDetailRequestData

internal class PinnedServicesDataSourceImpl(
    private val dataStore: DataStore<PinnedServices>
) : PinnedServicesDataSource {

    override val pinnedServices: Flow<List<PinnedServiceData>>
        get() = dataStore.data.map { it.services }

    override suspend fun addPinnedService(pinnedService: PinnedServiceData) {
        dataStore.updateData { currentPinnedServices ->
            val updatedServices = buildList {
                add(pinnedService)
                addAll(
                    currentPinnedServices.services.filterNot {
                        it.serviceDetailRequest == pinnedService.serviceDetailRequest
                    }
                )
            }

            currentPinnedServices.copy(services = updatedServices)
        }
    }

    override suspend fun removePinnedService(serviceDetailRequest: ServiceDetailRequestData) {
        dataStore.updateData { currentPinnedServices ->
            currentPinnedServices.copy(
                services = currentPinnedServices.services.filterNot {
                    it.serviceDetailRequest == serviceDetailRequest
                }
            )
        }
    }
}
