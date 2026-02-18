package me.cniekirk.ontrackapp.core.domain.usecase

import dev.zacsweers.metro.Inject
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceDetailRequest
import me.cniekirk.ontrackapp.core.domain.model.pinned.PinnedService
import me.cniekirk.ontrackapp.core.domain.repository.PinnedServicesRepository

@Inject
class PinServiceUseCase(
    private val pinnedServicesRepository: PinnedServicesRepository
) {

    suspend operator fun invoke(
        origin: String,
        destination: String,
        trainOperatingCompany: String,
        scheduledArrivalTime: String,
        serviceDetailRequest: ServiceDetailRequest,
    ): Result<Unit> {
        val pinnedService = PinnedService(
            origin = origin,
            destination = destination,
            trainOperatingCompany = trainOperatingCompany,
            scheduledArrivalTime = scheduledArrivalTime,
            serviceDetailRequest = serviceDetailRequest
        )

        return pinnedServicesRepository.pinService(pinnedService)
    }
}