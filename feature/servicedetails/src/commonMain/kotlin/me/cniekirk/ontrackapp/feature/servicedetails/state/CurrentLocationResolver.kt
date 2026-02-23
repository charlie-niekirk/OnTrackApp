package me.cniekirk.ontrackapp.feature.servicedetails.state

import co.touchlab.kermit.Logger
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.cniekirk.ontrackapp.core.domain.model.servicedetails.Location
import me.cniekirk.ontrackapp.core.domain.model.services.ServiceLocation
import me.cniekirk.ontrackapp.core.domain.model.services.TimeStatus
import kotlin.time.Clock

internal object CurrentLocationResolver {

    fun resolve(
        locations: List<Location>,
        nowMinutesOfDay: Int = currentMinutesOfDay()
    ): ServiceCurrentLocation? {
        if (locations.isEmpty()) return null

        val explicitServiceLocationIndex = locations.indexOfFirst { it.serviceLocation != null }

        Logger.d("Resolving current location for ${locations.size} locations, explicit index: $explicitServiceLocationIndex")

        if (explicitServiceLocationIndex >= 0) {
            return when (locations[explicitServiceLocationIndex].serviceLocation) {
                ServiceLocation.APPROACHING_STATION,
                ServiceLocation.APPROACHING_PLATFORM -> {
                    if (explicitServiceLocationIndex > 0) {
                        buildBetweenStationsLocation(
                            locations = locations,
                            fromIndex = explicitServiceLocationIndex - 1,
                            toIndex = explicitServiceLocationIndex,
                            nowMinutesOfDay = nowMinutesOfDay,
                            fallbackCloserTo = ServiceCurrentLocation.BetweenStations.CloserTo.TO
                        )
                    } else {
                        ServiceCurrentLocation.AtStation(explicitServiceLocationIndex)
                    }
                }

                ServiceLocation.AT_PLATFORM,
                ServiceLocation.PREPARING_DEPARTURE,
                ServiceLocation.READY_TO_DEPART -> ServiceCurrentLocation.AtStation(explicitServiceLocationIndex)

                null -> null
            }
        }

        for (index in 0 until locations.lastIndex) {
            val current = locations[index]
            val next = locations[index + 1]

            Logger.d("Checking location $index: ${current.departureTimeStatus} -> ${next.arrivalTimeStatus}")

            if (current.departureTimeStatus is TimeStatus.Departed &&
                next.arrivalTimeStatus !is TimeStatus.Arrived
            ) {
                return buildBetweenStationsLocation(
                    locations = locations,
                    fromIndex = index,
                    toIndex = index + 1,
                    nowMinutesOfDay = nowMinutesOfDay,
                    fallbackCloserTo = ServiceCurrentLocation.BetweenStations.CloserTo.FROM
                )
            }
        }

        val atStationIndex = locations.indexOfFirst { location ->
            location.arrivalTimeStatus is TimeStatus.Arrived &&
                location.departureTimeStatus !is TimeStatus.Departed
        }
        if (atStationIndex >= 0) {
            return ServiceCurrentLocation.AtStation(atStationIndex)
        }

        val lastVisitedIndex = locations.indexOfLast { location ->
            location.arrivalTimeStatus is TimeStatus.Arrived ||
                location.departureTimeStatus is TimeStatus.Departed
        }
        if (lastVisitedIndex >= 0) {
            return if (lastVisitedIndex == locations.lastIndex) {
                ServiceCurrentLocation.AtStation(lastVisitedIndex)
            } else {
                buildBetweenStationsLocation(
                    locations = locations,
                    fromIndex = lastVisitedIndex,
                    toIndex = lastVisitedIndex + 1,
                    nowMinutesOfDay = nowMinutesOfDay,
                    fallbackCloserTo = ServiceCurrentLocation.BetweenStations.CloserTo.FROM
                )
            }
        }

        val firstActiveIndex = locations.indexOfFirst { !it.isFullyInactive() }
        return ServiceCurrentLocation.AtStation(if (firstActiveIndex >= 0) firstActiveIndex else 0)
    }
}

private fun buildBetweenStationsLocation(
    locations: List<Location>,
    fromIndex: Int,
    toIndex: Int,
    nowMinutesOfDay: Int,
    fallbackCloserTo: ServiceCurrentLocation.BetweenStations.CloserTo
): ServiceCurrentLocation.BetweenStations {
    val closerTo = inferCloserStation(
        fromLocation = locations[fromIndex],
        toLocation = locations[toIndex],
        nowMinutesOfDay = nowMinutesOfDay
    ) ?: fallbackCloserTo

    return ServiceCurrentLocation.BetweenStations(
        fromIndex = fromIndex,
        toIndex = toIndex,
        closerTo = closerTo
    )
}

private fun inferCloserStation(
    fromLocation: Location,
    toLocation: Location,
    nowMinutesOfDay: Int
): ServiceCurrentLocation.BetweenStations.CloserTo? {
    val departureMinutes = fromLocation.departureTimeStatus.bestDepartureMinutes() ?: return null
    val arrivalMinutes = toLocation.arrivalTimeStatus.bestArrivalMinutes() ?: return null
    val totalGapMinutes = minutesUntil(startMinutes = departureMinutes, endMinutes = arrivalMinutes)
    if (totalGapMinutes <= 0) return null

    val elapsedSinceDeparture = minutesUntil(startMinutes = departureMinutes, endMinutes = nowMinutesOfDay)
    return if (elapsedSinceDeparture * 2 >= totalGapMinutes) {
        ServiceCurrentLocation.BetweenStations.CloserTo.TO
    } else {
        ServiceCurrentLocation.BetweenStations.CloserTo.FROM
    }
}

private fun TimeStatus.bestDepartureMinutes(): Int? {
    return when (this) {
        is TimeStatus.Departed -> parseMinutesOfDay(actualDepartureTime) ?: parseMinutesOfDay(scheduledDepartureTime)
        is TimeStatus.Delayed -> parseMinutesOfDay(estimatedTime) ?: parseMinutesOfDay(scheduledTime)
        is TimeStatus.OnTime -> parseMinutesOfDay(scheduledTime)
        is TimeStatus.Cancelled -> parseMinutesOfDay(scheduledDepartureTime)
        is TimeStatus.Arrived,
        is TimeStatus.Unknown -> null
    }
}

private fun TimeStatus.bestArrivalMinutes(): Int? {
    return when (this) {
        is TimeStatus.Arrived -> parseMinutesOfDay(actualArrivalTime) ?: parseMinutesOfDay(scheduledArrivalTime)
        is TimeStatus.Delayed -> parseMinutesOfDay(estimatedTime) ?: parseMinutesOfDay(scheduledTime)
        is TimeStatus.OnTime -> parseMinutesOfDay(scheduledTime)
        is TimeStatus.Cancelled -> parseMinutesOfDay(scheduledDepartureTime)
        is TimeStatus.Departed,
        is TimeStatus.Unknown -> null
    }
}

private fun parseMinutesOfDay(raw: String): Int? {
    val digits = raw.filter(Char::isDigit)
    if (digits.length != 4) return null

    val hours = digits.substring(0, 2).toIntOrNull() ?: return null
    val minutes = digits.substring(2, 4).toIntOrNull() ?: return null
    if (hours !in 0..23 || minutes !in 0..59) return null

    return hours * 60 + minutes
}

private fun minutesUntil(startMinutes: Int, endMinutes: Int): Int {
    val minutesInDay = 24 * 60
    return (endMinutes - startMinutes + minutesInDay) % minutesInDay
}

private fun currentMinutesOfDay(): Int {
    val localTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .time
    return localTime.hour * 60 + localTime.minute
}

private fun Location.isFullyInactive(): Boolean {
    val arrivalInactive = arrivalTimeStatus is TimeStatus.Unknown || arrivalTimeStatus is TimeStatus.Cancelled
    val departureInactive = departureTimeStatus is TimeStatus.Unknown || departureTimeStatus is TimeStatus.Cancelled
    return arrivalInactive && departureInactive
}
