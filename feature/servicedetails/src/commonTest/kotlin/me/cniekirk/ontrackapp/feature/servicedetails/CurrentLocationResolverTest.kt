package me.cniekirk.ontrackapp.feature.servicedetails

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import me.cniekirk.ontrackapp.core.domain.model.servicedetails.Location
import me.cniekirk.ontrackapp.core.domain.model.services.Platform
import me.cniekirk.ontrackapp.core.domain.model.services.ServiceLocation
import me.cniekirk.ontrackapp.core.domain.model.services.TimeStatus
import me.cniekirk.ontrackapp.feature.servicedetails.state.CurrentLocationResolver
import me.cniekirk.ontrackapp.feature.servicedetails.state.ServiceCurrentLocation

class CurrentLocationResolverTest {

    @Test
    fun `resolve returns between stations when approaching next station is explicitly provided`() {
        val locations = listOf(
            location(
                name = "Taunton",
                departure = TimeStatus.Departed(
                    actualDepartureTime = "1353",
                    scheduledDepartureTime = "1353",
                    delayInMinutes = 0
                )
            ),
            location(
                name = "Bristol Temple Meads",
                arrival = TimeStatus.OnTime("1424"),
                departure = TimeStatus.OnTime("1431"),
                serviceLocation = ServiceLocation.APPROACHING_STATION
            )
        )

        assertEquals(
            expected = ServiceCurrentLocation.BetweenStations(
                fromIndex = 0,
                toIndex = 1,
                closerTo = ServiceCurrentLocation.BetweenStations.CloserTo.TO
            ),
            actual = CurrentLocationResolver.resolve(
                locations = locations,
                nowMinutesOfDay = 14 * 60 + 10
            )
        )
    }

    @Test
    fun `resolve returns at station when service location is at platform`() {
        val locations = listOf(
            location(name = "Taunton"),
            location(
                name = "Bristol Temple Meads",
                serviceLocation = ServiceLocation.AT_PLATFORM
            )
        )

        assertEquals(
            expected = ServiceCurrentLocation.AtStation(index = 1),
            actual = CurrentLocationResolver.resolve(
                locations = locations,
                nowMinutesOfDay = 14 * 60 + 10
            )
        )
    }

    @Test
    fun `resolve returns between stations when train departed current stop but has not arrived next`() {
        val locations = listOf(
            location(
                name = "Taunton",
                departure = TimeStatus.Departed(
                    actualDepartureTime = "1353",
                    scheduledDepartureTime = "1353",
                    delayInMinutes = 0
                )
            ),
            location(
                name = "Bristol Temple Meads",
                arrival = TimeStatus.OnTime("1424"),
                departure = TimeStatus.OnTime("1431")
            ),
            location(
                name = "Bristol Parkway",
                arrival = TimeStatus.OnTime("1438"),
                departure = TimeStatus.OnTime("1440")
            )
        )

        assertEquals(
            expected = ServiceCurrentLocation.BetweenStations(
                fromIndex = 0,
                toIndex = 1,
                closerTo = ServiceCurrentLocation.BetweenStations.CloserTo.FROM
            ),
            actual = CurrentLocationResolver.resolve(
                locations = locations,
                nowMinutesOfDay = 14 * 60
            )
        )
    }

    @Test
    fun `resolve returns at station when train has arrived but not departed`() {
        val locations = listOf(
            location(
                name = "Reading",
                arrival = TimeStatus.Arrived(
                    actualArrivalTime = "1120",
                    scheduledArrivalTime = "1120",
                    delayInMinutes = 0
                ),
                departure = TimeStatus.OnTime("1125")
            ),
            location(
                name = "Didcot Parkway",
                arrival = TimeStatus.OnTime("1132"),
                departure = TimeStatus.OnTime("1133")
            )
        )

        assertEquals(
            expected = ServiceCurrentLocation.AtStation(index = 0),
            actual = CurrentLocationResolver.resolve(
                locations = locations,
                nowMinutesOfDay = 11 * 60 + 22
            )
        )
    }

    @Test
    fun `resolve returns null for empty location list`() {
        assertNull(CurrentLocationResolver.resolve(emptyList(), nowMinutesOfDay = 0))
    }

    @Test
    fun `resolve chooses to station when exactly halfway between stations`() {
        val locations = listOf(
            location(
                name = "Cheltenham Spa",
                departure = TimeStatus.Departed(
                    actualDepartureTime = "1600",
                    scheduledDepartureTime = "1600",
                    delayInMinutes = 0
                )
            ),
            location(
                name = "Bristol Parkway",
                arrival = TimeStatus.OnTime("1610"),
                departure = TimeStatus.OnTime("1612")
            )
        )

        assertEquals(
            expected = ServiceCurrentLocation.BetweenStations(
                fromIndex = 0,
                toIndex = 1,
                closerTo = ServiceCurrentLocation.BetweenStations.CloserTo.TO
            ),
            actual = CurrentLocationResolver.resolve(
                locations = locations,
                nowMinutesOfDay = 16 * 60 + 5
            )
        )
    }
}

private fun location(
    name: String,
    arrival: TimeStatus = TimeStatus.Unknown,
    departure: TimeStatus = TimeStatus.Unknown,
    serviceLocation: ServiceLocation? = null
): Location {
    return Location(
        locationName = name,
        departureTimeStatus = departure,
        arrivalTimeStatus = arrival,
        platform = Platform.Confirmed(platformName = "1", isChanged = false),
        serviceLocation = serviceLocation
    )
}
