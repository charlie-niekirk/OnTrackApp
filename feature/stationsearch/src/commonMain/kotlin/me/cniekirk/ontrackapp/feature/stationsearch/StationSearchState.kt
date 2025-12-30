package me.cniekirk.ontrackapp.feature.stationsearch

import me.cniekirk.ontrackapp.core.common.model.StationType
import me.cniekirk.ontrackapp.core.domain.model.Station

data class StationSearchState(
    val stationType: StationType,
    val isLoading: Boolean = true,
    val stations: List<Station> = emptyList(),
    val searchQuery: String = ""
)