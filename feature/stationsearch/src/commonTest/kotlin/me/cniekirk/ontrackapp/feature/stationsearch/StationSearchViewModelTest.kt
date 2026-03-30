package me.cniekirk.ontrackapp.feature.stationsearch

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import me.cniekirk.ontrackapp.core.common.model.StationType
import me.cniekirk.ontrackapp.core.domain.model.Station
import me.cniekirk.ontrackapp.core.domain.repository.StationsRepository
import org.orbitmvi.orbit.test.test
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StationSearchViewModelTest {

    private val stations = listOf(
        Station("WAT", "Waterloo"),
        Station("VIC", "Victoria"),
        Station("LBG", "London Bridge")
    )

    private val fakeRepository = object : StationsRepository {
        override suspend fun updateStations(): Result<List<Station>> = Result.success(stations)
        override suspend fun getStations(forceRefresh: Boolean): Result<List<Station>> = Result.success(stations)
    }

    @kotlin.test.BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @kotlin.test.AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun searchStations_debouncesFiltering() = runTest {
        val viewModel = StationSearchViewModel(fakeRepository, StationType.TARGET)

        // Initialize and verify initial fetch
        viewModel.test(this) {
            expectInitialState()
            runOnCreate()
            expectState { copy(isLoading = false, stations = stations) }

            // Fast typing "W", "Wa", "Wat"
            containerHost.searchStations("W")
            expectState { copy(searchQuery = "W") }

            // Advance time a little but less than debounce
            advanceTimeBy(100)

            containerHost.searchStations("Wa")
            expectState { copy(searchQuery = "Wa") }

            advanceTimeBy(100)

            containerHost.searchStations("Wat")
            expectState { copy(searchQuery = "Wat") }

            // Now advance past debounce time (assuming 300ms)
            advanceTimeBy(300)

            // Should now see filtering for "Wat"
            expectState { copy(stations = listOf(stations[0])) } // Waterloo matches Wat
        }
    }
}
