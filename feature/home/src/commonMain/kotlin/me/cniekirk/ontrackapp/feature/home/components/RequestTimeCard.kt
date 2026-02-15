package me.cniekirk.ontrackapp.feature.home.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import me.cniekirk.ontrackapp.core.designsystem.theme.OnTrackTheme
import me.cniekirk.ontrackapp.core.domain.model.arguments.RequestTime
import me.cniekirk.ontrackapp.core.domain.model.theme.ThemeMode
import ontrackapp.feature.home.generated.resources.Res
import ontrackapp.feature.home.generated.resources.now_departure_time
import ontrackapp.feature.home.generated.resources.set_departure_time
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun RequestTimeCard(
    requestTime: RequestTime,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AssistChip(
        modifier = modifier,
        onClick = onClick,
        label = {
            Text(text = requestTimeLabel(requestTime))
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null
            )
        }
    )
}

@Composable
private fun requestTimeLabel(requestTime: RequestTime): String {
    return when (requestTime) {
        RequestTime.Now -> stringResource(Res.string.now_departure_time)
        is RequestTime.AtTime -> stringResource(
            Res.string.set_departure_time,
            requestTime.day,
            requestTime.month,
            requestTime.year,
            requestTime.hours,
            requestTime.mins
        )
    }
}

@Preview
@Composable
private fun RequestTimeCardNowPreview() {
    OnTrackTheme(themeMode = ThemeMode.SYSTEM) {
        Surface {
            RequestTimeCard(
                requestTime = RequestTime.Now,
                onClick = {}
            )
        }
    }
}

@Preview
@Composable
private fun RequestTimeCardAtTimePreview() {
    OnTrackTheme(themeMode = ThemeMode.SYSTEM) {
        Surface {
            RequestTimeCard(
                requestTime = RequestTime.AtTime(
                    year = "2026",
                    month = "02",
                    day = "10",
                    hours = "18",
                    mins = "24"
                ),
                onClick = {}
            )
        }
    }
}
