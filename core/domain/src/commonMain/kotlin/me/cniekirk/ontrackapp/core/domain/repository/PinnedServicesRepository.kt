package me.cniekirk.ontrackapp.core.domain.repository

import kotlinx.coroutines.flow.Flow
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceDetailRequest
import me.cniekirk.ontrackapp.core.domain.model.pinned.PinnedService

interface PinnedServicesRepository {

    val pinnedServices: Flow<List<PinnedService>>

    suspend fun pinService(pinnedService: PinnedService): Result<Unit>

    suspend fun unpinService(serviceDetailRequest: ServiceDetailRequest): Result<Unit>
}
