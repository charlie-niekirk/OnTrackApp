package me.cniekirk.ontrackapp.core.datastore

import kotlinx.coroutines.flow.Flow
import me.cniekirk.ontrackapp.core.datastore.model.PinnedServiceData
import me.cniekirk.ontrackapp.core.datastore.model.ServiceDetailRequestData

interface PinnedServicesDataSource {

    val pinnedServices: Flow<List<PinnedServiceData>>

    suspend fun addPinnedService(pinnedService: PinnedServiceData)

    suspend fun removePinnedService(serviceDetailRequest: ServiceDetailRequestData)
}
