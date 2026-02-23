package me.cniekirk.ontrackapp.feature.servicedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceDetailRequest
import me.cniekirk.ontrackapp.core.domain.model.arguments.TrainStation
import me.cniekirk.ontrackapp.core.domain.repository.PinnedServicesRepository
import me.cniekirk.ontrackapp.core.domain.repository.RealtimeTrainsRepository
import me.cniekirk.ontrackapp.core.domain.usecase.PinServiceUseCase
import me.cniekirk.ontrackapp.core.domain.usecase.UnpinServiceUseCase
import me.cniekirk.ontrackapp.feature.servicedetails.state.CurrentLocationResolver
import me.cniekirk.ontrackapp.feature.servicedetails.state.ServiceDetailsEffect
import me.cniekirk.ontrackapp.feature.servicedetails.state.ServiceDetailsState
import me.cniekirk.ontrackapp.feature.servicedetails.state.TimelineRowStateMapper
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

@AssistedInject
class ServiceDetailsViewModel(
    @Assisted private val serviceDetailsRequest: ServiceDetailRequest,
    @Assisted private val targetStation: TrainStation,
    @Assisted private val filterStation: TrainStation?,
    private val realtimeTrainsRepository: RealtimeTrainsRepository,
    private val pinnedServicesRepository: PinnedServicesRepository,
    private val pinServiceUseCase: PinServiceUseCase,
    private val unpinServiceUseCase: UnpinServiceUseCase
) : ViewModel(), ContainerHost<ServiceDetailsState, ServiceDetailsEffect> {

    override val container = container<ServiceDetailsState, ServiceDetailsEffect>(
        ServiceDetailsState.Loading(
            targetStation = targetStation,
            filterStation = filterStation
        )
    ) {
        fetchServiceDetails()
        observePinnedState()
    }

    fun togglePinState() = intent {
        val currentState = state
        val pinResult = when (currentState) {
            is ServiceDetailsState.Loading,
            is ServiceDetailsState.Error -> {
                if (currentState.isPinned) {
                    unpinServiceUseCase(serviceDetailsRequest)
                } else {
                    return@intent
                }
            }

            is ServiceDetailsState.Ready -> {
                if (currentState.isPinned) {
                    unpinServiceUseCase(serviceDetailsRequest)
                } else {
                    pinServiceUseCase(
                        origin = currentState.origin,
                        destination = currentState.destination,
                        targetStation = currentState.targetStation,
                        filterStation = currentState.filterStation,
                        trainOperatingCompany = currentState.trainOperatingCompany,
                        scheduledArrivalTime = currentState.scheduledArrivalTime,
                        serviceDetailRequest = serviceDetailsRequest
                    )
                }
            }
        }

        pinResult.onFailure {
            postSideEffect(ServiceDetailsEffect.DisplayError)
        }
    }

    private fun observePinnedState() {
        viewModelScope.launch {
            pinnedServicesRepository.pinnedServices
                .map { pinnedServices ->
                    pinnedServices.any { it.serviceDetailRequest == serviceDetailsRequest }
                }
                .distinctUntilChanged()
                .collect { isPinned ->
                    intent {
                        reduce {
                            state.withPinnedState(isPinned)
                        }
                    }
                }
        }
    }

    fun fetchServiceDetails() = intent {
        reduce {
            ServiceDetailsState.Loading(
                targetStation = state.targetStation,
                filterStation = state.filterStation,
                isPinned = state.isPinned
            )
        }

        realtimeTrainsRepository.getServiceDetails(
            serviceDetailsRequest.serviceUid,
            serviceDetailsRequest.year,
            serviceDetailsRequest.month,
            serviceDetailsRequest.day
        ).onSuccess { serviceDetails ->
            val currentLocation = CurrentLocationResolver.resolve(serviceDetails.locations)

            reduce {
                ServiceDetailsState.Ready(
                    targetStation = state.targetStation,
                    filterStation = state.filterStation,
                    isPinned = state.isPinned,
                    origin = serviceDetails.origin,
                    destination = serviceDetails.destination,
                    currentLocation = currentLocation,
                    trainOperatingCompany = serviceDetails.trainOperatingCompany,
                    timelineRows = TimelineRowStateMapper.map(
                        locations = serviceDetails.locations,
                        currentLocation = currentLocation,
                        targetStation = state.targetStation,
                        filterStation = state.filterStation
                    ),
                    scheduledArrivalTime = serviceDetails.scheduledArrivalTime
                )
            }
        }.onFailure { throwable ->
            reduce {
                ServiceDetailsState.Error(
                    targetStation = state.targetStation,
                    filterStation = state.filterStation,
                    isPinned = state.isPinned,
                    errorType = throwable.message ?: "Unknown error"
                )
            }
            postSideEffect(ServiceDetailsEffect.DisplayError)
        }
    }

    private fun ServiceDetailsState.withPinnedState(isPinned: Boolean): ServiceDetailsState {
        return when (this) {
            is ServiceDetailsState.Loading -> copy(isPinned = isPinned)
            is ServiceDetailsState.Ready -> copy(isPinned = isPinned)
            is ServiceDetailsState.Error -> copy(isPinned = isPinned)
        }
    }

    @AssistedFactory
    @ManualViewModelAssistedFactoryKey(Factory::class)
    @ContributesIntoMap(AppScope::class)
    interface Factory : ManualViewModelAssistedFactory {
        fun create(
            serviceDetailsRequest: ServiceDetailRequest,
            targetStation: TrainStation,
            filterStation: TrainStation?
        ): ServiceDetailsViewModel
    }
}
