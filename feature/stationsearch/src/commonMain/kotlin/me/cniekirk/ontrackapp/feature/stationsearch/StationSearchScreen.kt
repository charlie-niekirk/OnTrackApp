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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import me.cniekirk.ontrackapp.core.common.model.StationResult
import me.cniekirk.ontrackapp.core.domain.model.Station
import me.cniekirk.ontrackapp.feature.stationsearch.components.StationList
import ontrackapp.feature.stationsearch.generated.resources.Res
import ontrackapp.feature.stationsearch.generated.resources.station_search_placeholder
import org.jetbrains.compose.resources.stringResource
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun StationSearchScreen(
    viewModel: StationSearchViewModel,
    onStationResult: (StationResult) -> Unit
) {
    val state = viewModel.collectAsState().value

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            else -> {}
        }
    }

    StationSearchScreenContent(
        state = state,
        onQueryChanged = viewModel::searchStations,
        onStationClicked = {
            Logger.d("Station CLICKED: CRS: ${it.crs}, NAME: ${it.name}")
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
        if (state.isLoading) {
            Spacer(modifier = Modifier.weight(1f))
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.weight(1f))
        } else {
            var searchText by remember { mutableStateOf("") }

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, start = 16.dp, end = 16.dp),
                value = searchText,
                onValueChange = {
                    searchText = it
                    onQueryChanged(it)
                },
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
