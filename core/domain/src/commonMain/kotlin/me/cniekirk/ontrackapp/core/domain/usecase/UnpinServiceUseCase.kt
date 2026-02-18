package me.cniekirk.ontrackapp.core.domain.usecase

import dev.zacsweers.metro.Inject
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceDetailRequest
import me.cniekirk.ontrackapp.core.domain.repository.PinnedServicesRepository

@Inject
class UnpinServiceUseCase(
    private val pinnedServicesRepository: PinnedServicesRepository
) {

    suspend operator fun invoke(serviceDetailRequest: ServiceDetailRequest): Result<Unit> {
        return pinnedServicesRepository.unpinService(serviceDetailRequest)
    }
}
