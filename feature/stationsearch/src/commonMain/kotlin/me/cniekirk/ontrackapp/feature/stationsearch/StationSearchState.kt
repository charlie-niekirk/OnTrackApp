package me.cniekirk.ontrackapp.feature.stationsearch

import me.cniekirk.ontrackapp.core.common.model.StationType
import me.cniekirk.ontrackapp.core.domain.model.Station

sealed interface StationSearchState {

    val stationType: StationType

    data class Loading(
        override val stationType: StationType,
    ) : StationSearchState

    data class Ready(
        override val stationType: StationType,
        val stations: List<Station> = emptyList(),
        val searchQuery: String = ""
    ) : StationSearchState

    data class Error(
        override val stationType: StationType,
        val errorType: String
    ) : StationSearchState
}
