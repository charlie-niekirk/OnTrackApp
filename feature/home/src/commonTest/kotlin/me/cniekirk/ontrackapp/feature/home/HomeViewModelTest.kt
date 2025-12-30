package me.cniekirk.ontrackapp.feature.home

import kotlinx.coroutines.test.runTest
import me.cniekirk.ontrackapp.core.common.model.StationType
import me.cniekirk.ontrackapp.core.domain.model.Station
import me.cniekirk.ontrackapp.core.domain.model.arguments.RequestTime
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceListRequest
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceListType
import me.cniekirk.ontrackapp.core.domain.model.arguments.TrainStation
import org.orbitmvi.orbit.test.test
import kotlin.test.Test

class HomeViewModelTest {

    private fun createViewModel(): HomeViewModel {
        return HomeViewModel()
    }

    @Test
    fun `updateQueryType updates state correctly`() = runTest {
        val viewModel = createViewModel()

        viewModel.test(this) {
            containerHost.updateQueryType(QueryType.ARRIVALS)

            expectState { copy(queryType = QueryType.ARRIVALS) }
        }
    }

    @Test
    fun `stationSelected with TARGET type updates targetStationSelection`() = runTest {
        val viewModel = createViewModel()
        val station = Station(crs = "KGX", name = "Kings Cross")

        viewModel.test(this) {
            containerHost.stationSelected(StationType.TARGET, station)

            expectState {
                copy(targetStationSelection = StationSelection.Selected(station))
            }
        }
    }

    @Test
    fun `stationSelected with FILTER type updates filterStationSelection`() = runTest {
        val viewModel = createViewModel()
        val station = Station(crs = "PAD", name = "Paddington")

        viewModel.test(this) {
            containerHost.stationSelected(StationType.FILTER, station)

            expectState {
                copy(filterStationSelection = StationSelection.Selected(station))
            }
        }
    }

    @Test
    fun `clearTargetStation resets targetStationSelection to None`() = runTest {
        val viewModel = createViewModel()
        val station = Station(crs = "KGX", name = "Kings Cross")

        viewModel.test(this) {
            containerHost.stationSelected(StationType.TARGET, station)

            expectState {
                copy(targetStationSelection = StationSelection.Selected(station))
            }

            containerHost.clearTargetStation()

            expectState {
                copy(targetStationSelection = StationSelection.None)
            }
        }
    }

    @Test
    fun `clearFilterStation resets filterStationSelection to None`() = runTest {
        val viewModel = createViewModel()
        val station = Station(crs = "PAD", name = "Paddington")

        viewModel.test(this) {
            containerHost.stationSelected(StationType.FILTER, station)

            expectState {
                copy(filterStationSelection = StationSelection.Selected(station))
            }

            containerHost.clearFilterStation()

            expectState {
                copy(filterStationSelection = StationSelection.None)
            }
        }
    }

    @Test
    fun `searchTrains with no target station shows error`() = runTest {
        val viewModel = createViewModel()

        viewModel.test(this) {
            containerHost.searchTrains()

            expectSideEffect(HomeEffect.ShowNoStationSelectedError)
        }
    }

    @Test
    fun `searchTrains with target station only navigates with correct ServiceListRequest for DEPARTURES`() = runTest {
        val viewModel = createViewModel()
        val targetStation = Station(crs = "KGX", name = "Kings Cross")
        val expectedServiceListRequest = ServiceListRequest(
            serviceListType = ServiceListType.DEPARTURES,
            requestTime = RequestTime.Now,
            targetStation = TrainStation(targetStation.crs, targetStation.name),
            filterStation = null
        )

        viewModel.test(this) {
            containerHost.stationSelected(StationType.TARGET, targetStation)

            expectState {
                copy(targetStationSelection = StationSelection.Selected(targetStation))
            }

            containerHost.searchTrains()

            expectSideEffect(HomeEffect.NavigateToServiceList(expectedServiceListRequest))
        }
    }

    @Test
    fun `searchTrains with target station only navigates with correct ServiceListRequest for ARRIVALS`() = runTest {
        val viewModel = createViewModel()
        val targetStation = Station(crs = "KGX", name = "Kings Cross")
        val arrivalsQueryType = QueryType.ARRIVALS

        val expectedServiceListRequest = ServiceListRequest(
            serviceListType = ServiceListType.ARRIVALS,
            requestTime = RequestTime.Now,
            targetStation = TrainStation(targetStation.crs, targetStation.name),
            filterStation = null
        )

        viewModel.test(this) {
            containerHost.updateQueryType(arrivalsQueryType)

            expectState { copy(queryType = arrivalsQueryType) }

            containerHost.stationSelected(StationType.TARGET, targetStation)

            expectState { copy(targetStationSelection = StationSelection.Selected(targetStation)) }

            containerHost.searchTrains()

            expectSideEffect(HomeEffect.NavigateToServiceList(expectedServiceListRequest))
        }
    }

    @Test
    fun `searchTrains with target and filter stations navigates with both stations for DEPARTURES`() = runTest {
        val viewModel = createViewModel()
        val targetStation = Station(crs = "KGX", name = "Kings Cross")
        val filterStation = Station(crs = "PAD", name = "Paddington")

        val expectedServiceListRequest = ServiceListRequest(
            serviceListType = ServiceListType.DEPARTURES,
            requestTime = RequestTime.Now,
            targetStation = TrainStation(targetStation.crs, targetStation.name),
            filterStation = TrainStation(filterStation.crs, filterStation.name)
        )

        viewModel.test(this) {
            containerHost.stationSelected(StationType.TARGET, targetStation)

            expectState { copy(targetStationSelection = StationSelection.Selected(targetStation)) }

            containerHost.stationSelected(StationType.FILTER, filterStation)

            expectState { copy(filterStationSelection = StationSelection.Selected(filterStation)) }

            containerHost.searchTrains()

            expectSideEffect(HomeEffect.NavigateToServiceList(expectedServiceListRequest))
        }
    }

    @Test
    fun `searchTrains with target and filter stations navigates with both stations for ARRIVALS`() = runTest {
        val viewModel = createViewModel()
        val targetStation = Station(crs = "KGX", name = "Kings Cross")
        val filterStation = Station(crs = "PAD", name = "Paddington")
        val arrivalsQueryType = QueryType.ARRIVALS

        val expectedServiceListRequest = ServiceListRequest(
            serviceListType = ServiceListType.ARRIVALS,
            requestTime = RequestTime.Now,
            targetStation = TrainStation(targetStation.crs, targetStation.name),
            filterStation = TrainStation(filterStation.crs, filterStation.name)
        )

        viewModel.test(this) {
            containerHost.updateQueryType(arrivalsQueryType)

            expectState { copy(queryType = arrivalsQueryType) }

            containerHost.stationSelected(StationType.TARGET, targetStation)

            expectState { copy(targetStationSelection = StationSelection.Selected(targetStation)) }

            containerHost.stationSelected(StationType.FILTER, filterStation)

            expectState { copy(filterStationSelection = StationSelection.Selected(filterStation)) }

            containerHost.searchTrains()

            expectSideEffect(HomeEffect.NavigateToServiceList(expectedServiceListRequest))
        }
    }

    @Test
    fun `multiple station selections update state correctly`() = runTest {
        val viewModel = createViewModel()
        val firstTargetStation = Station(crs = "KGX", name = "Kings Cross")
        val secondTargetStation = Station(crs = "VIC", name = "Victoria")
        val filterStation = Station(crs = "PAD", name = "Paddington")

        viewModel.test(this) {
            // Select first target station
            containerHost.stationSelected(StationType.TARGET, firstTargetStation)

            expectState { copy(targetStationSelection = StationSelection.Selected(firstTargetStation)) }

            // Update to second target station
            containerHost.stationSelected(StationType.TARGET, secondTargetStation)

            expectState { copy(targetStationSelection = StationSelection.Selected(secondTargetStation)) }

            // Add filter station
            containerHost.stationSelected(StationType.FILTER, filterStation)

            expectState { copy(filterStationSelection = StationSelection.Selected(filterStation)) }
        }
    }

    @Test
    fun `clearing stations after selection works correctly`() = runTest {
        val viewModel = createViewModel()
        val targetStation = Station(crs = "KGX", name = "Kings Cross")
        val filterStation = Station(crs = "PAD", name = "Paddington")

        viewModel.test(this) {
            // Select both stations
            containerHost.stationSelected(StationType.TARGET, targetStation)

            expectState { copy(targetStationSelection = StationSelection.Selected(targetStation)) }

            containerHost.stationSelected(StationType.FILTER, filterStation)

            expectState { copy(filterStationSelection = StationSelection.Selected(filterStation)) }

            // Clear target station
            containerHost.clearTargetStation()

            expectState { copy(targetStationSelection = StationSelection.None) }

            // Clear filter station
            containerHost.clearFilterStation()

            expectState { copy(filterStationSelection = StationSelection.None) }
        }
    }
}
