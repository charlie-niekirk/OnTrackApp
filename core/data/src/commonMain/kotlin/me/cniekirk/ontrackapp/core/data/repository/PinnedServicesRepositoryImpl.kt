package me.cniekirk.ontrackapp.core.data.repository

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.cniekirk.ontrackapp.core.data.mapper.toDataModel
import me.cniekirk.ontrackapp.core.data.mapper.toDomainModel
import me.cniekirk.ontrackapp.core.datastore.PinnedServicesDataSource
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceDetailRequest
import me.cniekirk.ontrackapp.core.domain.model.pinned.PinnedService
import me.cniekirk.ontrackapp.core.domain.repository.PinnedServicesRepository

@ContributesBinding(AppScope::class)
@Inject
class PinnedServicesRepositoryImpl(
    private val pinnedServicesDataSource: PinnedServicesDataSource
) : PinnedServicesRepository {

    override val pinnedServices: Flow<List<PinnedService>>
        get() = pinnedServicesDataSource.pinnedServices.map { pinnedServices ->
            pinnedServices.map { it.toDomainModel() }
        }

    override suspend fun pinService(pinnedService: PinnedService): Result<Unit> {
        return runCatching {
            pinnedServicesDataSource.addPinnedService(pinnedService.toDataModel())
        }
    }

    override suspend fun unpinService(serviceDetailRequest: ServiceDetailRequest): Result<Unit> {
        return runCatching {
            pinnedServicesDataSource.removePinnedService(serviceDetailRequest.toDataModel())
        }
    }
}
