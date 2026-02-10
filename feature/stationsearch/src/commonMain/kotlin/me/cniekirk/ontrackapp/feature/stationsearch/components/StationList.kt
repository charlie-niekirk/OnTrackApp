package me.cniekirk.ontrackapp.feature.stationsearch.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import me.cniekirk.ontrackapp.core.domain.model.Station

@Composable
fun StationList(
    stations: List<Station>,
    onStationClicked: (Station) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold { innerPadding ->
        LazyColumn(
            modifier = modifier.consumeWindowInsets(innerPadding),
            contentPadding = innerPadding
        ) {
            items(stations) { station ->
                StationListItem(
                    station = station,
                    onStationClicked = onStationClicked
                )
            }
        }
    }
}

@Composable
fun StationListItem(
    station: Station,
    onStationClicked: (Station) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onStationClicked(station) }
            .padding(16.dp)
    ) {
        Text(
            text = station.name,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            modifier = Modifier.alpha(0.8f),
            text = station.crs,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}