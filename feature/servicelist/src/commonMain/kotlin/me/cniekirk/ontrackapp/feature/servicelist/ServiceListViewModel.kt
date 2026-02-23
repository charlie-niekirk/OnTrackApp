package me.cniekirk.ontrackapp.feature.servicelist

import androidx.lifecycle.ViewModel
import co.touchlab.kermit.Logger
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import me.cniekirk.ontrackapp.core.domain.model.arguments.RequestTime
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceDetailRequest
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceListRequest
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceListType
import me.cniekirk.ontrackapp.core.domain.model.services.TrainService
import me.cniekirk.ontrackapp.core.domain.repository.RealtimeTrainsRepository
import me.cniekirk.ontrackapp.core.domain.repository.RecentSearchesRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container

@AssistedInject
class ServiceListViewModel(
    @Assisted private val serviceListRequest: ServiceListRequest,
    private val realtimeTrainsRepository: RealtimeTrainsRepository,
    private val recentSearchesRepository: RecentSearchesRepository
) : ViewModel(), ContainerHost<ServiceListState, ServiceListEffect> {

    private val loadingState = ServiceListState.Loading(
        serviceListType = serviceListRequest.serviceListType,
        targetStation = serviceListRequest.targetStation.name,
        filterStation = serviceListRequest.filterStation?.name
    )

    override val container = container<ServiceListState, ServiceListEffect>(loadingState) {
        when (state) {
            is ServiceListState.Loading -> {
                getTrainList()
                cacheRecentSearch()
            }
            is ServiceListState.Error -> {

            }
            is ServiceListState.Ready -> Unit
        }
    }

    fun refreshTrainList() = intent {
        (state as? ServiceListState.Ready)?.let { readyState ->
            reduce {
                ServiceListState.Ready(
                    serviceListType = serviceListRequest.serviceListType,
                    targetStation = serviceListRequest.targetStation.name,
                    filterStation = serviceListRequest.filterStation?.name,
                    trainServiceList = readyState.trainServiceList,
                    isRefreshing = true,
                )
            }
            getTrainList()
        }
    }

    @OptIn(OrbitExperimental::class)
    private suspend fun getTrainList() = subIntent {
        fetchTrainServices()
            .onSuccess { trainServiceList ->
                reduce {
                    ServiceListState.Ready(
                        trainServiceList = trainServiceList,
                        serviceListType = serviceListRequest.serviceListType,
                        targetStation = serviceListRequest.targetStation.name,
                        filterStation = serviceListRequest.filterStation?.name
                    )
                }
            }
            .onFailure {
                reduce {
                    ServiceListState.Error(
                        serviceListType = serviceListRequest.serviceListType,
                        targetStation = serviceListRequest.targetStation.name,
                        filterStation = serviceListRequest.filterStation?.name,
                        errorType = it.message ?: "Unknown error"
                    )
                }
            }
    }

    private fun cacheRecentSearch() = intent {
        recentSearchesRepository.cacheRecentSearch(serviceListRequest)
            .onSuccess {
                Logger.v("Cached recent search successfully: $serviceListRequest")
            }
            .onFailure {
                Logger.e("Recent search cache failed: $serviceListRequest, error: $it")
            }
    }

    fun serviceSelected(trainService: TrainService) = intent {
        postSideEffect(
            ServiceListEffect.NavigateToServiceDetails(
                serviceDetailRequest = ServiceDetailRequest(
                    serviceUid = trainService.serviceId,
                    year = trainService.runDate.year,
                    month = trainService.runDate.month,
                    day = trainService.runDate.day,
                    serviceListType = serviceListRequest.serviceListType
                ),
                targetStation = serviceListRequest.targetStation,
                filterStation = serviceListRequest.filterStation
            )
        )
    }

    private suspend fun fetchTrainServices(): Result<List<TrainService>> {
        val filterStation = serviceListRequest.filterStation
        return when (val requestTime = serviceListRequest.requestTime) {
            is RequestTime.AtTime -> {
                when (serviceListRequest.serviceListType) {
                    ServiceListType.DEPARTURES -> {
                        if (filterStation != null) {
                            realtimeTrainsRepository.getDepartureBoardOnDateTimeTo(
                                fromStation = serviceListRequest.targetStation.crs,
                                toStation = filterStation.crs,
                                year = requestTime.year,
                                month = requestTime.month,
                                day = requestTime.day,
                                time = requestTime.hours + requestTime.mins
                            )
                        } else {
                            realtimeTrainsRepository.getDepartureBoardOnDateTime(
                                station = serviceListRequest.targetStation.crs,
                                year = requestTime.year,
                                month = requestTime.month,
                                day = requestTime.day,
                                time = requestTime.hours + requestTime.mins
                            )
                        }
                    }
                    ServiceListType.ARRIVALS -> {
                        if (filterStation != null) {
                            realtimeTrainsRepository.getArrivalBoardOnDateTimeFrom(
                                atStation = serviceListRequest.targetStation.crs,
                                fromStation = filterStation.crs,
                                year = requestTime.year,
                                month = requestTime.month,
                                day = requestTime.day,
                                time = requestTime.hours + requestTime.mins
                            )
                        } else {
                            realtimeTrainsRepository.getArrivalBoardOnDateTime(
                                station = serviceListRequest.targetStation.crs,
                                year = requestTime.year,
                                month = requestTime.month,
                                day = requestTime.day,
                                time = requestTime.hours + requestTime.mins
                            )
                        }
                    }
                }
            }
            is RequestTime.Now -> {
                when (serviceListRequest.serviceListType) {
                    ServiceListType.DEPARTURES -> {
                        if (filterStation != null) {
                            realtimeTrainsRepository.getCurrentDepartureBoardTo(
                                fromStation = serviceListRequest.targetStation.crs,
                                toStation = filterStation.crs
                            )
                        } else {
                            realtimeTrainsRepository.getCurrentDepartureBoard(
                                station = serviceListRequest.targetStation.crs
                            )
                        }
                    }
                    ServiceListType.ARRIVALS -> {
                        if (filterStation != null) {
                            realtimeTrainsRepository.getCurrentArrivalBoardFrom(
                                atStation = serviceListRequest.targetStation.crs,
                                fromStation = filterStation.crs
                            )
                        } else {
                            realtimeTrainsRepository.getCurrentArrivalBoard(
                                station = serviceListRequest.targetStation.crs
                            )
                        }
                    }
                }
            }
        }
    }

    @AssistedFactory
    @ManualViewModelAssistedFactoryKey(Factory::class)
    @ContributesIntoMap(AppScope::class)
    interface Factory : ManualViewModelAssistedFactory {
        fun create(serviceListRequest: ServiceListRequest): ServiceListViewModel
    }
}
