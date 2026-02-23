package me.cniekirk.ontrackapp.feature.servicedetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
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
import me.cniekirk.ontrackapp.feature.servicedetails.state.ServiceCurrentLocation
import me.cniekirk.ontrackapp.feature.servicedetails.state.ServiceDetailsEffect
import me.cniekirk.ontrackapp.feature.servicedetails.state.ServiceDetailsState
import me.cniekirk.ontrackapp.feature.servicedetails.state.TimelineRowStateMapper
import ontrackapp.feature.servicedetails.generated.resources.Res
import ontrackapp.feature.servicedetails.generated.resources.pin_service
import ontrackapp.feature.servicedetails.generated.resources.unpin_service
import org.jetbrains.compose.resources.stringResource
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

    ServiceDetailsScreen(
        state = state.value,
        onPinStateToggled = viewModel::togglePinState
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ServiceDetailsScreen(
    state: ServiceDetailsState,
    onPinStateToggled: () -> Unit
) {
    val timelineListState = rememberLazyListState()
    val readyState = state as? ServiceDetailsState.Ready
    val currentLocationRowIndex = readyState?.let { currentState ->
        when (val currentLocation = currentState.currentLocation) {
            is ServiceCurrentLocation.AtStation -> currentLocation.index
            is ServiceCurrentLocation.BetweenStations -> currentLocation.fromIndex
            null -> null
        }?.takeIf { currentState.timelineRows.isNotEmpty() }
            ?.coerceIn(0, currentState.timelineRows.lastIndex)
    }

    LaunchedEffect(currentLocationRowIndex) {
        currentLocationRowIndex?.let { timelineListState.animateScrollToItem(index = it) }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
    ) { innerPadding ->
        when (state) {
            is ServiceDetailsState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ServiceDetailsState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error loading service details: ${state.errorType}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            is ServiceDetailsState.Ready -> {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    RouteHeader(
                        modifier = Modifier.padding(top = 16.dp),
                        origin = state.origin,
                        destination = state.destination,
                        trainOperatingCompany = state.trainOperatingCompany
                    )

                    ToggleButton(
                        modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp),
                        checked = state.isPinned,
                        onCheckedChange = { onPinStateToggled() }
                    ) {
                        val toggleText = if (state.isPinned) {
                            stringResource(Res.string.unpin_service)
                        } else {
                            stringResource(Res.string.pin_service)
                        }

                        Text(text = toggleText)
                    }

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
    val state = ServiceDetailsState.Ready(
        origin = "London Paddington",
        destination = "Taunton",
        currentLocation = currentLocation,
        timelineRows = TimelineRowStateMapper.map(
            locations = locations,
            currentLocation = currentLocation,
            targetStation = TrainStation(crs = "RDG", name = "Reading"),
            filterStation = null
        ),
        trainOperatingCompany = "Great Western Railway",
        targetStation = TrainStation(crs = "RDG", name = "Reading"),
        scheduledArrivalTime = "11:20"
    )

    OnTrackTheme(themeMode = ThemeMode.SYSTEM) {
        Surface {
            ServiceDetailsScreen(
                state = state,
                onPinStateToggled = {}
            )
        }
    }
}
