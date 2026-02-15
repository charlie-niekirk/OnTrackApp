package me.cniekirk.ontrackapp.feature.servicedetails.state

sealed interface ServiceCurrentLocation {
    data class AtStation(val index: Int) : ServiceCurrentLocation
    data class BetweenStations(
        val fromIndex: Int,
        val toIndex: Int,
        val closerTo: CloserTo
    ) : ServiceCurrentLocation {
        enum class CloserTo {
            FROM,
            TO
        }
    }
}
