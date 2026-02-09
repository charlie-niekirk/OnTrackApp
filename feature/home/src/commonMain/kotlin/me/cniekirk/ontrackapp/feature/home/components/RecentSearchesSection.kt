package me.cniekirk.ontrackapp.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.cniekirk.ontrackapp.core.designsystem.theme.OnTrackTheme
import me.cniekirk.ontrackapp.core.domain.model.arguments.RequestTime
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceListRequest
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceListType
import me.cniekirk.ontrackapp.core.domain.model.arguments.TrainStation
import me.cniekirk.ontrackapp.core.domain.model.theme.ThemeMode
import ontrackapp.feature.home.generated.resources.Res
import ontrackapp.feature.home.generated.resources.arrivals_title
import ontrackapp.feature.home.generated.resources.arriving_prefix
import ontrackapp.feature.home.generated.resources.clear_recent_searches
import ontrackapp.feature.home.generated.resources.departing_prefix
import ontrackapp.feature.home.generated.resources.departures_title
import ontrackapp.feature.home.generated.resources.no_recent_searches
import ontrackapp.feature.home.generated.resources.recent_search_filter
import ontrackapp.feature.home.generated.resources.recent_searches_title
import org.jetbrains.compose.resources.stringResource

private const val MAX_RECENT_SEARCHES_TO_SHOW = 5

@Composable
internal fun RecentSearchesSection(
    recentSearches: List<ServiceListRequest>,
    modifier: Modifier = Modifier,
    onRecentSearchClicked: (ServiceListRequest) -> Unit,
    onClearClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.recent_searches_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )

            TextButton(
                onClick = onClearClicked,
                enabled = recentSearches.isNotEmpty()
            ) {
                Text(text = stringResource(Res.string.clear_recent_searches))
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = RoundedCornerShape(20.dp)
        ) {
            if (recentSearches.isEmpty()) {
                Text(
                    text = stringResource(Res.string.no_recent_searches),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)
                )
            } else {
                Column(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    recentSearches
                        .take(MAX_RECENT_SEARCHES_TO_SHOW)
                        .forEach { request ->
                            RecentSearchItem(
                                modifier = Modifier.clickable { onRecentSearchClicked(request) },
                                request = request
                            )
                        }
                }
            }
        }
    }
}

@Composable
private fun RecentSearchItem(
    request: ServiceListRequest,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        val type = when (request.serviceListType) {
            ServiceListType.DEPARTURES -> stringResource(Res.string.departing_prefix)
            ServiceListType.ARRIVALS -> stringResource(Res.string.arriving_prefix)
        }

        Column {
            Text(
                text = type,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = request.targetStation.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
        }

        val filterText = request.filterStation?.name?.let { stationName ->
            stringResource(Res.string.recent_search_filter, stationName)
        }

        if (filterText != null) {
            Text(
                text = filterText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
private fun RecentSearchesSectionPreview() {
    OnTrackTheme(themeMode = ThemeMode.SYSTEM) {
        Surface {
            RecentSearchesSection(
                modifier = Modifier.padding(8.dp),
                recentSearches = listOf(
                    ServiceListRequest(
                        serviceListType = ServiceListType.DEPARTURES,
                        requestTime = RequestTime.Now,
                        targetStation = TrainStation(crs = "LBG", name = "London Bridge"),
                        filterStation = TrainStation(crs = "NWX", name = "New Cross")
                    )
                ),
                onRecentSearchClicked = {},
                onClearClicked = {}
            )
        }
    }
}
