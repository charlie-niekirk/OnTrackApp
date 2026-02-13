package me.cniekirk.ontrackapp.feature.servicedetails

import androidx.lifecycle.ViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceDetailRequest
import me.cniekirk.ontrackapp.core.domain.repository.RealtimeTrainsRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

@AssistedInject
class ServiceDetailsViewModel(
    @Assisted private val serviceDetailsRequest: ServiceDetailRequest,
    private val realtimeTrainsRepository: RealtimeTrainsRepository
) : ViewModel(), ContainerHost<ServiceDetailsState, ServiceDetailsEffect> {

    override val container = container<ServiceDetailsState, ServiceDetailsEffect>(ServiceDetailsState()) {
        fetchServiceDetails()
    }

    fun fetchServiceDetails() = intent {
        realtimeTrainsRepository.getServiceDetails(
            serviceDetailsRequest.serviceUid,
            serviceDetailsRequest.year,
            serviceDetailsRequest.month,
            serviceDetailsRequest.day
        ).onSuccess {

        }.onFailure {

        }
    }

    @AssistedFactory
    @ManualViewModelAssistedFactoryKey(Factory::class)
    @ContributesIntoMap(AppScope::class)
    interface Factory : ManualViewModelAssistedFactory {
        fun create(serviceDetailsRequest: ServiceDetailRequest): ServiceDetailsViewModel
    }
}