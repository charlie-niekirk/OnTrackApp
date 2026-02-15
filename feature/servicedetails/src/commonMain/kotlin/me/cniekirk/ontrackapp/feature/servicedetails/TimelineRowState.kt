package me.cniekirk.ontrackapp.feature.servicedetails

import me.cniekirk.ontrackapp.core.domain.model.arguments.TrainStation
import me.cniekirk.ontrackapp.core.domain.model.servicedetails.Location

data class TimelineRowState(
    val location: Location,
    val emphasiseStation: Boolean,
    val emphasiseLineAbove: Boolean,
    val emphasiseLineBelow: Boolean,
    val marker: TimelineMarker
)

sealed interface TimelineMarker {
    data object None : TimelineMarker
    data object AtStation : TimelineMarker
    data class BetweenStations(
        val placement: Placement
    ) : TimelineMarker

    enum class Placement {
        FROM_SIDE,
        TO_SIDE
    }
}

internal object TimelineRowStateMapper {

    fun map(
        locations: List<Location>,
        currentLocation: ServiceCurrentLocation?,
        targetStation: TrainStation,
        filterStation: TrainStation?
    ): List<TimelineRowState> {
        val emphasisRange = resolveEmphasisRange(
            locations = locations,
            targetStation = targetStation,
            filterStation = filterStation
        )

        return locations.mapIndexed { index, location ->
            TimelineRowState(
                location = location,
                emphasiseStation = emphasisRange?.contains(index) == true,
                emphasiseLineAbove = emphasisRange?.let { (index - 1) in it && index in it } == true,
                emphasiseLineBelow = emphasisRange?.let { index in it && (index + 1) in it } == true,
                marker = markerFor(index, currentLocation)
            )
        }
    }

    private fun markerFor(
        index: Int,
        currentLocation: ServiceCurrentLocation?
    ): TimelineMarker {
        return when (currentLocation) {
            is ServiceCurrentLocation.AtStation -> {
                if (currentLocation.index == index) TimelineMarker.AtStation else TimelineMarker.None
            }

            is ServiceCurrentLocation.BetweenStations -> {
                when (currentLocation.closerTo) {
                    ServiceCurrentLocation.BetweenStations.CloserTo.FROM -> {
                        if (currentLocation.fromIndex == index) {
                            TimelineMarker.BetweenStations(TimelineMarker.Placement.FROM_SIDE)
                        } else {
                            TimelineMarker.None
                        }
                    }

                    ServiceCurrentLocation.BetweenStations.CloserTo.TO -> {
                        if (currentLocation.toIndex == index) {
                            TimelineMarker.BetweenStations(TimelineMarker.Placement.TO_SIDE)
                        } else {
                            TimelineMarker.None
                        }
                    }
                }
            }

            null -> TimelineMarker.None
        }
    }

    private fun resolveEmphasisRange(
        locations: List<Location>,
        targetStation: TrainStation,
        filterStation: TrainStation?
    ): IntRange? {
        val targetIndex = findStationIndex(locations, targetStation)
        if (targetIndex == -1) return null

        if (filterStation == null) {
            return targetIndex..locations.lastIndex
        }

        val filterIndex = findStationIndex(locations, filterStation)
        if (filterIndex == -1) {
            return targetIndex..targetIndex
        }

        return minOf(targetIndex, filterIndex)..maxOf(targetIndex, filterIndex)
    }

    private fun findStationIndex(
        locations: List<Location>,
        station: TrainStation
    ): Int {
        val stationCode = station.crs.normaliseStationCode()
        if (stationCode != null) {
            val codeMatchIndex = locations.indexOfFirst { it.crs.normaliseStationCode() == stationCode }
            if (codeMatchIndex != -1) return codeMatchIndex
        }

        val stationName = station.name.normaliseStationName()
        return locations.indexOfFirst { it.locationName.normaliseStationName() == stationName }
    }
}

private fun String.normaliseStationName(): String = trim().lowercase()

private fun String?.normaliseStationCode(): String? {
    return this?.trim()?.takeIf { it.isNotEmpty() }?.lowercase()
}
