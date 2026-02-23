package me.cniekirk.ontrackapp.feature.pinned

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.cniekirk.ontrackapp.core.domain.model.pinned.PinnedService
import ontrackapp.feature.pinned.generated.resources.Res
import ontrackapp.feature.pinned.generated.resources.pinned_empty
import ontrackapp.feature.pinned.generated.resources.pinned_operator
import ontrackapp.feature.pinned.generated.resources.pinned_route
import ontrackapp.feature.pinned.generated.resources.pinned_title
import org.jetbrains.compose.resources.stringResource
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun PinnedRoute(
    viewModel: PinnedViewModel,
    onServiceSelected: (PinnedService) -> Unit
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is PinnedEffect.NavigateToServiceDetails -> onServiceSelected(sideEffect.pinnedService)
        }
    }

    PinnedScreen(
        state = state,
        onServiceSelected = viewModel::serviceSelected
    )
}

@Composable
private fun PinnedScreen(
    state: PinnedState,
    onServiceSelected: (PinnedService) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                text = stringResource(Res.string.pinned_title),
                style = MaterialTheme.typography.titleLarge
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                when (state) {
                    PinnedState.Loading -> CircularProgressIndicator()

                    is PinnedState.Error -> {
                        Text(
                            text = "Error loading pinned services: ${state.errorType}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    is PinnedState.Ready -> {
                        if (state.pinnedServices.isEmpty()) {
                            Text(
                                text = stringResource(Res.string.pinned_empty),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(state.pinnedServices) { pinnedService ->
                                    PinnedServiceListItem(
                                        pinnedService = pinnedService,
                                        onServiceSelected = onServiceSelected
                                    )
                                    HorizontalDivider()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PinnedServiceListItem(
    pinnedService: PinnedService,
    onServiceSelected: (PinnedService) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onServiceSelected(pinnedService) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Text(
            text = pinnedService.scheduledArrivalTime,
            style = MaterialTheme.typography.bodyMedium
        )

        Column(
            modifier = Modifier.padding(start = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(Res.string.pinned_route, pinnedService.origin, pinnedService.destination),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = stringResource(Res.string.pinned_operator, pinnedService.trainOperatingCompany),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
