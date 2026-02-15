package me.cniekirk.ontrackapp.feature.home

import androidx.lifecycle.ViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import me.cniekirk.ontrackapp.core.common.model.StationType
import me.cniekirk.ontrackapp.core.domain.model.Station
import me.cniekirk.ontrackapp.core.domain.model.arguments.RequestTime
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceListRequest
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceListType
import me.cniekirk.ontrackapp.core.domain.model.arguments.TrainStation
import me.cniekirk.ontrackapp.core.domain.repository.RecentSearchesRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

@Inject
@ViewModelKey(HomeViewModel::class)
@ContributesIntoMap(scope = AppScope::class, binding = binding<ViewModel>())
class HomeViewModel(
    private val recentSearchesRepository: RecentSearchesRepository
) : ViewModel(), ContainerHost<HomeState, HomeEffect> {

    override val container = container<HomeState, HomeEffect>(HomeState(recentSearches = recentSearchesRepository.recentSearches))

    fun updateQueryType(queryType: QueryType) = intent {
        reduce {
            state.copy(queryType = queryType)
        }
    }

    fun updateRequestTime(requestTime: RequestTime) = intent {
        reduce {
            state.copy(requestTime = requestTime)
        }
    }

    fun stationSelected(stationType: StationType, station: Station) = intent {
        when (stationType) {
            StationType.TARGET -> {
                reduce {
                    state.copy(targetStationSelection = StationSelection.Selected(station))
                }
            }
            StationType.FILTER -> {
                reduce {
                    state.copy(filterStationSelection = StationSelection.Selected(station))
                }
            }
        }
    }

    fun clearTargetStation() = intent {
        reduce {
            state.copy(targetStationSelection = StationSelection.None)
        }
    }

    fun clearFilterStation() = intent {
        reduce {
            state.copy(filterStationSelection = StationSelection.None)
        }
    }

    fun searchTrains() = intent {
        when (val targetStation = state.targetStationSelection) {
            is StationSelection.None -> {
                // Invalid, post error
                postSideEffect(HomeEffect.ShowNoStationSelectedError)
            }
            is StationSelection.Selected -> {
                val serviceListRequest = when (val filterStation = state.filterStationSelection) {
                    is StationSelection.None -> {
                        ServiceListRequest(
                            serviceListType = if (state.queryType == QueryType.DEPARTURES) ServiceListType.DEPARTURES else ServiceListType.ARRIVALS,
                            requestTime = state.requestTime,
                            targetStation = TrainStation(
                                targetStation.station.crs,
                                targetStation.station.name
                            ),
                            filterStation = null
                        )
                    }
                    is StationSelection.Selected -> {
                        ServiceListRequest(
                            serviceListType = if (state.queryType == QueryType.DEPARTURES) ServiceListType.DEPARTURES else ServiceListType.ARRIVALS,
                            requestTime = state.requestTime,
                            targetStation = TrainStation(
                                targetStation.station.crs,
                                targetStation.station.name
                            ),
                            filterStation = TrainStation(
                                filterStation.station.crs,
                                filterStation.station.name
                            )
                        )
                    }
                }

                recentSearchesRepository.cacheRecentSearch(serviceListRequest)
                postSideEffect(HomeEffect.NavigateToServiceList(serviceListRequest))
            }
        }
    }

    fun recentSearchClicked(serviceListRequest: ServiceListRequest) = intent {
        postSideEffect(HomeEffect.NavigateToServiceList(serviceListRequest))
    }

    fun clearRecentSearches() = intent {
        recentSearchesRepository.deleteAllRecentSearches()
    }
}
