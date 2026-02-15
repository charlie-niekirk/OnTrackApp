package me.cniekirk.ontrackapp.feature.servicedetails.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.cniekirk.ontrackapp.core.designsystem.preview.PreviewDayNight
import me.cniekirk.ontrackapp.core.designsystem.theme.OnTrackTheme
import me.cniekirk.ontrackapp.core.domain.model.theme.ThemeMode
import ontrackapp.feature.servicedetails.generated.resources.Res
import ontrackapp.feature.servicedetails.generated.resources.route_format
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun RouteHeader(
    origin: String,
    destination: String,
    trainOperatingCompany: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(Res.string.route_format, origin, destination),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 2
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = trainOperatingCompany,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@PreviewDayNight
@Composable
private fun RouteHeaderPreview() {
    OnTrackTheme(themeMode = ThemeMode.SYSTEM) {
        Surface {
            RouteHeader(
                origin = "London Paddington",
                destination = "Taunton",
                trainOperatingCompany = "Great Western Railway"
            )
        }
    }
}
