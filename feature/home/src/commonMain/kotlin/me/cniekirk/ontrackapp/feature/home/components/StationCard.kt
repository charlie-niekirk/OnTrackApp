package me.cniekirk.ontrackapp.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.cniekirk.ontrackapp.core.domain.model.Station
import me.cniekirk.ontrackapp.feature.home.StationSelection
import ontrackapp.feature.home.generated.resources.Res
import ontrackapp.feature.home.generated.resources.cd_clear_station
import ontrackapp.feature.home.generated.resources.empty_departing_station
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun StationCard(
    stationSelection: StationSelection,
    placeholder: StringResource,
    onClick: () -> Unit,
    onClearSelectionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val queryStationText = when (stationSelection) {
            is StationSelection.None -> stringResource(placeholder)
            is StationSelection.Selected -> stationSelection.station.name
        }
        Text(
            modifier = Modifier.padding(
                start = 16.dp,
                top = 12.dp,
                bottom = 12.dp
            ),
            text = queryStationText,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(modifier = Modifier.padding(end = 8.dp)) {
            when (stationSelection) {
                is StationSelection.None -> {
                    IconButton(onClick = onClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                            contentDescription = null
                        )
                    }
                }
                is StationSelection.Selected -> {
                    IconButton(onClick = onClearSelectionClick) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(Res.string.cd_clear_station)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun StationCardSelectedPreview() {
    MaterialTheme {
        Surface {
            StationCard(
                stationSelection = StationSelection.Selected(Station("London Bridge", "LBG")),
                placeholder = Res.string.empty_departing_station,
                onClick = {},
                onClearSelectionClick = {}
            )
        }
    }
}

@Preview
@Composable
private fun StationCardNonePreview() {
    MaterialTheme {
        Surface {
            StationCard(
                stationSelection = StationSelection.None,
                placeholder = Res.string.empty_departing_station,
                onClick = {},
                onClearSelectionClick = {}
            )
        }
    }
}