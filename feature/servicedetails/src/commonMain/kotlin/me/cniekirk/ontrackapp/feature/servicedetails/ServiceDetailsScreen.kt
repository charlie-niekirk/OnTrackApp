package me.cniekirk.ontrackapp.feature.servicedetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.cniekirk.ontrackapp.core.designsystem.preview.PreviewDayNight
import me.cniekirk.ontrackapp.core.designsystem.theme.OnTrackTheme
import me.cniekirk.ontrackapp.core.domain.model.arguments.TrainStation
import me.cniekirk.ontrackapp.core.domain.model.servicedetails.Location
import me.cniekirk.ontrackapp.core.domain.model.services.Platform
import me.cniekirk.ontrackapp.core.domain.model.services.TimeStatus
import me.cniekirk.ontrackapp.core.domain.model.theme.ThemeMode
import me.cniekirk.ontrackapp.feature.servicedetails.components.LocationTimeline
import me.cniekirk.ontrackapp.feature.servicedetails.components.RouteHeader
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun ServiceDetailsRoute(viewModel: ServiceDetailsViewModel) {
    val state = viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            ServiceDetailsEffect.DisplayError -> Unit
        }
    }

    ServiceDetailsScreen(state.value)
}

@Composable
private fun ServiceDetailsScreen(state: ServiceDetailsState) {
    val timelineListState = rememberLazyListState()
    val currentLocationRowIndex = when (val currentLocation = state.currentLocation) {
        is ServiceCurrentLocation.AtStation -> currentLocation.index
        is ServiceCurrentLocation.BetweenStations -> currentLocation.fromIndex
        null -> null
    }?.takeIf { state.timelineRows.isNotEmpty() }?.coerceIn(0, state.timelineRows.lastIndex)

    LaunchedEffect(currentLocationRowIndex) {
        currentLocationRowIndex?.let { timelineListState.animateScrollToItem(index = it) }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            RouteHeader(
                modifier = Modifier.padding(top = 16.dp),
                origin = state.origin,
                destination = state.destination,
                trainOperatingCompany = state.trainOperatingCompany
            )

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LocationTimeline(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 12.dp),
                    contentPadding = innerPadding,
                    timelineRows = state.timelineRows,
                    listState = timelineListState
                )
            }
        }
    }
}

@PreviewDayNight
@Composable
private fun ServiceDetailsScreenPreview() {
    val locations = listOf(
        Location(
            locationName = "London Paddington",
            departureTimeStatus = TimeStatus.Departed(
                actualDepartureTime = "11:00",
                scheduledDepartureTime = "11:00",
                delayInMinutes = 0
            ),
            arrivalTimeStatus = TimeStatus.Unknown,
            platform = Platform.Confirmed(platformName = "1", isChanged = false),
            serviceLocation = null
        ),
        Location(
            locationName = "Reading",
            departureTimeStatus = TimeStatus.OnTime("11:25"),
            arrivalTimeStatus = TimeStatus.OnTime("11:20"),
            platform = Platform.Estimated(platformName = "5", isChanged = false),
            serviceLocation = null
        )
    )
    val currentLocation = ServiceCurrentLocation.BetweenStations(
        fromIndex = 0,
        toIndex = 1,
        closerTo = ServiceCurrentLocation.BetweenStations.CloserTo.TO
    )
    val state = ServiceDetailsState(
        isLoading = false,
        origin = "London Paddington",
        destination = "Taunton",
        currentLocation = currentLocation,
        timelineRows = TimelineRowStateMapper.map(
            locations = locations,
            currentLocation = currentLocation,
            targetStation = TrainStation(crs = "RDG", name = "Reading"),
            filterStation = null
        ),
        trainOperatingCompany = "Great Western Railway"
    )

    OnTrackTheme(themeMode = ThemeMode.SYSTEM) {
        Surface {
            ServiceDetailsScreen(state = state)
        }
    }
}
