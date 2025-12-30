package me.cniekirk.ontrackapp.feature.home

import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceListRequest

sealed interface HomeEffect {

    data class NavigateToServiceList(
        val serviceListRequest: ServiceListRequest
    ) : HomeEffect

    data object ShowNoStationSelectedError : HomeEffect

    data object ShowFailedToFetchRecentSearchesError : HomeEffect

    data object ShowFailedToClearRecentSearchesError : HomeEffect
}