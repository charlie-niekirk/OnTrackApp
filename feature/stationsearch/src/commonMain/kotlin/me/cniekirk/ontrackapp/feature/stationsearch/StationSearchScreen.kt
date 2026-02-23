package me.cniekirk.ontrackapp.feature.stationsearch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.cniekirk.ontrackapp.core.common.model.StationResult
import me.cniekirk.ontrackapp.core.domain.model.Station
import me.cniekirk.ontrackapp.feature.stationsearch.components.StationList
import ontrackapp.feature.stationsearch.generated.resources.Res
import ontrackapp.feature.stationsearch.generated.resources.station_fetch_error
import ontrackapp.feature.stationsearch.generated.resources.station_search_placeholder
import org.jetbrains.compose.resources.stringResource
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun StationSearchScreen(
    viewModel: StationSearchViewModel,
    onStationResult: (StationResult) -> Unit
) {
    val state = viewModel.collectAsState().value

    StationSearchScreenContent(
        state = state,
        onQueryChanged = viewModel::searchStations,
        onStationClicked = {
            onStationResult(
                StationResult(
                    stationType = state.stationType,
                    crs = it.crs,
                    name = it.name
                )
            )
        }
    )
}

@Composable
private fun StationSearchScreenContent(
    state: StationSearchState,
    onQueryChanged: (String) -> Unit,
    onStationClicked: (Station) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (state) {
            is StationSearchState.Loading -> {
                Spacer(modifier = Modifier.weight(1f))
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                Spacer(modifier = Modifier.weight(1f))
            }

            is StationSearchState.Error -> {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = stringResource(Res.string.station_fetch_error),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            is StationSearchState.Ready -> {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp, start = 16.dp, end = 16.dp),
                    value = state.searchQuery,
                    onValueChange = onQueryChanged,
                    placeholder = {
                        Text(text = stringResource(Res.string.station_search_placeholder))
                    }
                )

                StationList(
                    modifier = Modifier.padding(top = 16.dp),
                    stations = state.stations,
                    onStationClicked = onStationClicked
                )
            }
        }
    }
}
