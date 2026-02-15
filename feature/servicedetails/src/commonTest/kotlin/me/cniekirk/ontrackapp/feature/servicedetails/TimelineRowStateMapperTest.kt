package me.cniekirk.ontrackapp.feature.servicedetails

import kotlin.test.Test
import kotlin.test.assertEquals
import me.cniekirk.ontrackapp.core.domain.model.arguments.TrainStation
import me.cniekirk.ontrackapp.core.domain.model.servicedetails.Location
import me.cniekirk.ontrackapp.core.domain.model.services.Platform
import me.cniekirk.ontrackapp.core.domain.model.services.TimeStatus

class TimelineRowStateMapperTest {

    @Test
    fun `map emphasises stations and connectors between target and filter inclusive`() {
        val rows = TimelineRowStateMapper.map(
            locations = listOf(
                location("Taunton", "TAU"),
                location("Bristol Temple Meads", "BRI"),
                location("Bristol Parkway", "BPW"),
                location("Cardiff Central", "CDF")
            ),
            currentLocation = null,
            targetStation = TrainStation(crs = "BRI", name = "Bristol Temple Meads"),
            filterStation = TrainStation(crs = "CDF", name = "Cardiff Central")
        )

        assertEquals(
            expected = listOf(false, true, true, true),
            actual = rows.map { it.emphasiseStation }
        )
        assertEquals(
            expected = listOf(false, false, true, true),
            actual = rows.map { it.emphasiseLineAbove }
        )
        assertEquals(
            expected = listOf(false, true, true, false),
            actual = rows.map { it.emphasiseLineBelow }
        )
    }

    @Test
    fun `map emphasises from target to end when filter station is null`() {
        val rows = TimelineRowStateMapper.map(
            locations = listOf(
                location("Taunton", "TAU"),
                location("Bristol Temple Meads", "BRI"),
                location("Bristol Parkway", "BPW"),
                location("Cardiff Central", "CDF")
            ),
            currentLocation = null,
            targetStation = TrainStation(crs = "BPW", name = "Bristol Parkway"),
            filterStation = null
        )

        assertEquals(
            expected = listOf(false, false, true, true),
            actual = rows.map { it.emphasiseStation }
        )
        assertEquals(
            expected = listOf(false, false, false, true),
            actual = rows.map { it.emphasiseLineAbove }
        )
        assertEquals(
            expected = listOf(false, false, true, false),
            actual = rows.map { it.emphasiseLineBelow }
        )
    }

    @Test
    fun `map falls back to station names when CRS code is missing in locations`() {
        val rows = TimelineRowStateMapper.map(
            locations = listOf(
                location("Taunton"),
                location("Reading"),
                location("Didcot Parkway")
            ),
            currentLocation = null,
            targetStation = TrainStation(crs = "RDG", name = "Reading"),
            filterStation = TrainStation(crs = "DID", name = "Didcot Parkway")
        )

        assertEquals(
            expected = listOf(false, true, true),
            actual = rows.map { it.emphasiseStation }
        )
    }
}

private fun location(
    name: String,
    crs: String? = null
): Location {
    return Location(
        locationName = name,
        departureTimeStatus = TimeStatus.Unknown,
        arrivalTimeStatus = TimeStatus.Unknown,
        platform = Platform.Confirmed(platformName = "1", isChanged = false),
        serviceLocation = null,
        crs = crs
    )
}
