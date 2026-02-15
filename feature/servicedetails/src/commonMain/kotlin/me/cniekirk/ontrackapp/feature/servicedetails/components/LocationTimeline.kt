package me.cniekirk.ontrackapp.feature.servicedetails.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.cniekirk.ontrackapp.core.domain.model.servicedetails.Location
import me.cniekirk.ontrackapp.core.domain.model.services.Platform
import me.cniekirk.ontrackapp.core.domain.model.services.ServiceLocation
import me.cniekirk.ontrackapp.core.domain.model.services.TimeStatus
import me.cniekirk.ontrackapp.feature.servicedetails.state.TimelineMarker
import me.cniekirk.ontrackapp.feature.servicedetails.state.TimelineRowState
import ontrackapp.feature.servicedetails.generated.resources.Res
import ontrackapp.feature.servicedetails.generated.resources.arrived_time
import ontrackapp.feature.servicedetails.generated.resources.cancelled
import ontrackapp.feature.servicedetails.generated.resources.confirmed_platform
import ontrackapp.feature.servicedetails.generated.resources.departed_time
import ontrackapp.feature.servicedetails.generated.resources.estimated_platform
import ontrackapp.feature.servicedetails.generated.resources.service_location_approaching_platform
import ontrackapp.feature.servicedetails.generated.resources.service_location_approaching_station
import ontrackapp.feature.servicedetails.generated.resources.service_location_at_platform
import ontrackapp.feature.servicedetails.generated.resources.service_location_preparing_departure
import ontrackapp.feature.servicedetails.generated.resources.service_location_ready_to_depart
import ontrackapp.feature.servicedetails.generated.resources.time_with_delay
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun LocationTimeline(
    timelineRows: List<TimelineRowState>,
    contentPadding: PaddingValues,
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        state = listState,
        contentPadding = contentPadding
    ) {
        itemsIndexed(timelineRows) { index, row ->
            val markerType = when (val marker = row.marker) {
                TimelineMarker.None -> MarkerType.NONE
                TimelineMarker.AtStation -> MarkerType.AT_STATION
                is TimelineMarker.BetweenStations -> {
                    when (marker.placement) {
                        TimelineMarker.Placement.FROM_SIDE -> MarkerType.BETWEEN_FROM_STATION
                        TimelineMarker.Placement.TO_SIDE -> MarkerType.BETWEEN_TO_STATION
                    }
                }
            }

            LocationRow(
                location = row.location,
                markerType = markerType,
                isFirst = index == 0,
                isLast = index == timelineRows.lastIndex,
                emphasiseStation = row.emphasiseStation,
                emphasiseLineAbove = row.emphasiseLineAbove,
                emphasiseLineBelow = row.emphasiseLineBelow
            )
        }
    }
}

@Composable
private fun LocationRow(
    location: Location,
    markerType: MarkerType,
    isFirst: Boolean,
    isLast: Boolean,
    emphasiseStation: Boolean,
    emphasiseLineAbove: Boolean,
    emphasiseLineBelow: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TimelineIndicator(
            modifier = Modifier
                .width(24.dp)
                .fillMaxHeight()
                .padding(horizontal = 4.dp),
            isFirst = isFirst,
            isLast = isLast,
            markerType = markerType,
            emphasiseStation = emphasiseStation,
            emphasiseLineAbove = emphasiseLineAbove,
            emphasiseLineBelow = emphasiseLineBelow
        )

        Column(
            modifier = Modifier
                .padding(start = 12.dp, top = 8.dp, bottom = 8.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = location.locationName,
                style = MaterialTheme.typography.bodyLarge,
                color = if (emphasiseStation) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                fontWeight = if (emphasiseStation) FontWeight.Bold else FontWeight.Medium
            )

            TimeStatusText(timeStatus = location.primaryTimeStatus())

            PlatformText(location.platform)

            location.serviceLocation?.let { serviceLocation ->
                Text(
                    text = getServiceLocationText(serviceLocation),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun TimelineIndicator(
    isFirst: Boolean,
    isLast: Boolean,
    markerType: MarkerType,
    emphasiseStation: Boolean,
    emphasiseLineAbove: Boolean,
    emphasiseLineBelow: Boolean,
    modifier: Modifier = Modifier
) {
    val trackColor = MaterialTheme.colorScheme.outline
    val emphasisedTrackColor = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
        Color.Black
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    val stationColor = if (emphasiseStation) emphasisedTrackColor else trackColor
    val currentLocationColor = Color(0xFF1E88E5)

    val shouldAnimate = markerType != MarkerType.NONE
    val pulseScale: Float
    val pulseAlpha: Float
    if (shouldAnimate) {
        val pulseTransition = rememberInfiniteTransition(label = "current-location-pulse")
        val animatedPulseScale by pulseTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.9f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "current-location-pulse-scale"
        )
        val animatedPulseAlpha by pulseTransition.animateFloat(
            initialValue = 0.35f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "current-location-pulse-alpha"
        )
        pulseScale = animatedPulseScale
        pulseAlpha = animatedPulseAlpha
    } else {
        pulseScale = 1f
        pulseAlpha = 0f
    }

    Canvas(
        modifier = modifier
    ) {
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val lineWidth = 2.dp.toPx()
        val stationRadius = 4.dp.toPx()
        val dotRadius = 5.dp.toPx()
        val indicatorRadius = 8.dp.toPx()

        if (!isFirst) {
            drawLine(
                color = if (emphasiseLineAbove) emphasisedTrackColor else trackColor,
                start = Offset(centerX, 0f),
                end = Offset(centerX, centerY - stationRadius),
                strokeWidth = lineWidth
            )
        }

        if (!isLast) {
            drawLine(
                color = if (emphasiseLineBelow) emphasisedTrackColor else trackColor,
                start = Offset(centerX, centerY + stationRadius),
                end = Offset(centerX, size.height),
                strokeWidth = lineWidth
            )
        }

        drawCircle(
            color = stationColor,
            radius = stationRadius,
            center = Offset(centerX, centerY)
        )

        when (markerType) {
            MarkerType.AT_STATION -> {
                drawCircle(
                    color = currentLocationColor.copy(alpha = pulseAlpha),
                    radius = indicatorRadius * pulseScale,
                    center = Offset(centerX, centerY)
                )
                drawCircle(
                    color = currentLocationColor,
                    radius = indicatorRadius,
                    center = Offset(centerX, centerY)
                )
            }

            MarkerType.BETWEEN_FROM_STATION -> {
                val lineStart = centerY + stationRadius
                val lineEnd = size.height
                val betweenCenterY = ((lineStart + lineEnd) / 2f).coerceIn(dotRadius, size.height - dotRadius)
                drawCircle(
                    color = currentLocationColor.copy(alpha = pulseAlpha),
                    radius = indicatorRadius * pulseScale,
                    center = Offset(centerX, betweenCenterY)
                )
                drawCircle(
                    color = currentLocationColor,
                    radius = indicatorRadius,
                    center = Offset(centerX, betweenCenterY)
                )
            }

            MarkerType.BETWEEN_TO_STATION -> {
                val lineStart = 0f
                val lineEnd = centerY - stationRadius
                val betweenCenterY = ((lineStart + lineEnd) / 2f).coerceIn(dotRadius, size.height - dotRadius)
                drawCircle(
                    color = currentLocationColor.copy(alpha = pulseAlpha),
                    radius = indicatorRadius * pulseScale,
                    center = Offset(centerX, betweenCenterY)
                )
                drawCircle(
                    color = currentLocationColor,
                    radius = indicatorRadius,
                    center = Offset(centerX, betweenCenterY)
                )
            }

            MarkerType.NONE -> Unit
        }
    }
}

@Composable
private fun TimeStatusText(
    timeStatus: TimeStatus,
    modifier: Modifier = Modifier
) {
    val (text, color) = when (timeStatus) {
        is TimeStatus.Departed -> {
            val timeText = stringResource(Res.string.departed_time, timeStatus.actualDepartureTime)
            val finalText = if (timeStatus.delayInMinutes > 0) {
                stringResource(Res.string.time_with_delay, timeText, timeStatus.delayInMinutes)
            } else {
                timeText
            }
            finalText to if (timeStatus.delayInMinutes > 0) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.onBackground
            }
        }

        is TimeStatus.Arrived -> {
            val timeText = stringResource(Res.string.arrived_time, timeStatus.actualArrivalTime)
            val finalText = if (timeStatus.delayInMinutes > 0) {
                stringResource(Res.string.time_with_delay, timeText, timeStatus.delayInMinutes)
            } else {
                timeText
            }
            finalText to if (timeStatus.delayInMinutes > 0) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.onBackground
            }
        }

        is TimeStatus.OnTime -> {
            timeStatus.scheduledTime to MaterialTheme.colorScheme.onBackground
        }

        is TimeStatus.Delayed -> {
            val text = stringResource(
                Res.string.time_with_delay,
                timeStatus.estimatedTime,
                timeStatus.delayInMinutes
            )
            text to MaterialTheme.colorScheme.error
        }

        is TimeStatus.Cancelled -> {
            stringResource(Res.string.cancelled) to MaterialTheme.colorScheme.error
        }

        is TimeStatus.Unknown -> {
            "" to MaterialTheme.colorScheme.onBackground
        }
    }

    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = color,
        fontWeight = FontWeight.Medium
    )
}

@Composable
private fun PlatformText(platform: Platform) {
    val (textColor, fontWeight, platformName) = when (platform) {
        is Platform.Confirmed -> {
            if (platform.isChanged) {
                Triple(
                    MaterialTheme.colorScheme.error,
                    FontWeight.Bold,
                    stringResource(Res.string.confirmed_platform, platform.platformName)
                )
            } else {
                Triple(
                    MaterialTheme.colorScheme.onBackground,
                    FontWeight.Bold,
                    stringResource(Res.string.confirmed_platform, platform.platformName)
                )
            }
        }

        is Platform.Estimated -> {
            if (platform.isChanged) {
                Triple(
                    MaterialTheme.colorScheme.error,
                    FontWeight.Normal,
                    stringResource(Res.string.estimated_platform, platform.platformName)
                )
            } else {
                Triple(
                    MaterialTheme.colorScheme.onBackground,
                    FontWeight.Normal,
                    stringResource(Res.string.estimated_platform, platform.platformName)
                )
            }
        }
    }

    Text(
        text = platformName,
        style = MaterialTheme.typography.bodySmall,
        color = textColor,
        fontWeight = fontWeight
    )
}

private fun Location.primaryTimeStatus(): TimeStatus {
    return when (departureTimeStatus) {
        TimeStatus.Unknown -> arrivalTimeStatus
        else -> departureTimeStatus
    }
}

@Composable
private fun getServiceLocationText(serviceLocation: ServiceLocation): String {
    return when (serviceLocation) {
        ServiceLocation.APPROACHING_STATION -> stringResource(Res.string.service_location_approaching_station)
        ServiceLocation.APPROACHING_PLATFORM -> stringResource(Res.string.service_location_approaching_platform)
        ServiceLocation.AT_PLATFORM -> stringResource(Res.string.service_location_at_platform)
        ServiceLocation.PREPARING_DEPARTURE -> stringResource(Res.string.service_location_preparing_departure)
        ServiceLocation.READY_TO_DEPART -> stringResource(Res.string.service_location_ready_to_depart)
    }
}

private enum class MarkerType {
    NONE,
    AT_STATION,
    BETWEEN_FROM_STATION,
    BETWEEN_TO_STATION
}
