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
import me.cniekirk.ontrackapp.feature.servicedetails.state.CurrentLocationResolver
import me.cniekirk.ontrackapp.feature.servicedetails.state.ServiceDetailsEffect
import me.cniekirk.ontrackapp.feature.servicedetails.state.ServiceDetailsState
import me.cniekirk.ontrackapp.feature.servicedetails.state.TimelineRowStateMapper
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

@AssistedInject
class ServiceDetailsViewModel(
    @Assisted private val serviceDetailsRequest: ServiceDetailRequest,
    private val realtimeTrainsRepository: RealtimeTrainsRepository
) : ViewModel(), ContainerHost<ServiceDetailsState, ServiceDetailsEffect> {

    override val container = container<ServiceDetailsState, ServiceDetailsEffect>(
        ServiceDetailsState(
            targetStation = serviceDetailsRequest.targetStation,
            filterStation = serviceDetailsRequest.filterStation
        )
    ) {
        fetchServiceDetails()
    }

    fun fetchServiceDetails() = intent {
        reduce { state.copy(isLoading = true) }

        realtimeTrainsRepository.getServiceDetails(
            serviceDetailsRequest.serviceUid,
            serviceDetailsRequest.year,
            serviceDetailsRequest.month,
            serviceDetailsRequest.day
        ).onSuccess { serviceDetails ->
            val currentLocation = CurrentLocationResolver.resolve(serviceDetails.locations)
            reduce {
                state.copy(
                    isLoading = false,
                    origin = serviceDetails.origin,
                    destination = serviceDetails.destination,
                    currentLocation = currentLocation,
                    trainOperatingCompany = serviceDetails.trainOperatingCompany,
                    timelineRows = TimelineRowStateMapper.map(
                        locations = serviceDetails.locations,
                        currentLocation = currentLocation,
                        targetStation = serviceDetailsRequest.targetStation,
                        filterStation = serviceDetailsRequest.filterStation
                    )
                )
            }
        }.onFailure {
            reduce { state.copy(isLoading = false) }
            postSideEffect(ServiceDetailsEffect.DisplayError)
        }
    }

    @AssistedFactory
    @ManualViewModelAssistedFactoryKey(Factory::class)
    @ContributesIntoMap(AppScope::class)
    interface Factory : ManualViewModelAssistedFactory {
        fun create(serviceDetailsRequest: ServiceDetailRequest): ServiceDetailsViewModel
    }
}
