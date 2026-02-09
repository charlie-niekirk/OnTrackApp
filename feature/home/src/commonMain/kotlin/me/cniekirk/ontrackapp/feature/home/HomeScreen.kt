package me.cniekirk.ontrackapp.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import me.cniekirk.ontrackapp.core.common.model.StationType
import me.cniekirk.ontrackapp.core.designsystem.theme.OnTrackTheme
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceListRequest
import me.cniekirk.ontrackapp.core.domain.model.theme.ThemeMode
import me.cniekirk.ontrackapp.feature.home.components.DepartingArrivingButtonGroup
import me.cniekirk.ontrackapp.feature.home.components.RecentSearchesSection
import me.cniekirk.ontrackapp.feature.home.components.StationCard
import me.cniekirk.ontrackapp.feature.home.preview.HomeScreenPreviewParameterProvider
import ontrackapp.feature.home.generated.resources.Res
import ontrackapp.feature.home.generated.resources.button_search
import ontrackapp.feature.home.generated.resources.empty_arriving_filter_station
import ontrackapp.feature.home.generated.resources.empty_arriving_station
import ontrackapp.feature.home.generated.resources.empty_departing_filter_station
import ontrackapp.feature.home.generated.resources.empty_departing_station
import ontrackapp.feature.home.generated.resources.train_times_title
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.orbitmvi.orbit.compose.collectAsState as collectAsStateOrbit
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun HomeScreen(
    viewModel: HomeViewModel,
    navigateToStationSelection: (StationType) -> Unit,
    navigateToServiceList: (ServiceListRequest) -> Unit
) {
    val state = viewModel.collectAsStateOrbit()
    val recentSearches by state.value.recentSearches.collectAsState(initial = emptyList())

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is HomeEffect.NavigateToServiceList -> {
                navigateToServiceList(sideEffect.serviceListRequest)
            }
            else -> {}
        }
    }

    HomeScreenContent(
        state = state.value,
        recentSearches = recentSearches,
        onQueryTypeChanged = viewModel::updateQueryType,
        onTargetStationClicked = { navigateToStationSelection(StationType.TARGET) },
        onFilterStationClicked = { navigateToStationSelection(StationType.FILTER) },
        onClearTargetStationClicked = viewModel::clearTargetStation,
        onClearFilterStationClicked = viewModel::clearFilterStation,
        onSearchClicked = viewModel::searchTrains,
        onRecentSearchClicked = viewModel::recentSearchClicked,
        onClearRecentSearchesClicked = viewModel::clearRecentSearches
    )
}

@Composable
private fun HomeScreenContent(
    state: HomeState,
    recentSearches: List<ServiceListRequest>,
    onQueryTypeChanged: (QueryType) -> Unit,
    onTargetStationClicked: () -> Unit,
    onFilterStationClicked: () -> Unit,
    onClearTargetStationClicked: () -> Unit,
    onClearFilterStationClicked: () -> Unit,
    onSearchClicked: () -> Unit,
    onRecentSearchClicked: (ServiceListRequest) -> Unit,
    onClearRecentSearchesClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp),
            text = stringResource(Res.string.train_times_title),
            style = MaterialTheme.typography.headlineMedium
        )

        DepartingArrivingButtonGroup(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            queryType = state.queryType,
            onQueryTypeChanged = { onQueryTypeChanged(it) }
        )

        StationCard(
            stationSelection = state.targetStationSelection,
            placeholder = getPlaceholderText(isFilter = false, queryType = state.queryType),
            onClick = { onTargetStationClicked() },
            onClearSelectionClick = { onClearTargetStationClicked() }
        )

        StationCard(
            modifier = Modifier.padding(top = 16.dp),
            stationSelection = state.filterStationSelection,
            placeholder = getPlaceholderText(isFilter = true, queryType = state.queryType),
            onClick = { onFilterStationClicked() },
            onClearSelectionClick = { onClearFilterStationClicked() }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))

            FilledTonalButton(
                onClick = { onSearchClicked() }
            ) {
                Text(
                    text = stringResource(Res.string.button_search)
                )
            }
        }

        RecentSearchesSection(
            recentSearches = recentSearches,
            onRecentSearchClicked = onRecentSearchClicked,
            onClearClicked = onClearRecentSearchesClicked
        )
    }
}

private fun getPlaceholderText(isFilter: Boolean, queryType: QueryType): StringResource {
    return when (queryType) {
        QueryType.DEPARTURES -> {
            if (isFilter) {
                Res.string.empty_departing_filter_station
            } else {
                Res.string.empty_departing_station
            }
        }
        QueryType.ARRIVALS -> {
            if (isFilter) {
                Res.string.empty_arriving_filter_station
            } else {
                Res.string.empty_arriving_station
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun HomeScreenPreview(
    @PreviewParameter(HomeScreenPreviewParameterProvider::class) state: HomeState
) {
    OnTrackTheme(themeMode = ThemeMode.SYSTEM) {
        Surface {
            HomeScreenContent(
                state = state,
                recentSearches = emptyList(),
                onQueryTypeChanged = {},
                onTargetStationClicked = {},
                onFilterStationClicked = {},
                onClearTargetStationClicked = {},
                onClearFilterStationClicked = {},
                onSearchClicked = {},
                onRecentSearchClicked = {},
                onClearRecentSearchesClicked = {},
            )
        }
    }
}
