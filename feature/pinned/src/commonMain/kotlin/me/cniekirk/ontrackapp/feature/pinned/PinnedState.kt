package me.cniekirk.ontrackapp.feature.pinned

import me.cniekirk.ontrackapp.core.domain.model.pinned.PinnedService

sealed interface PinnedState {

    data object Loading : PinnedState

    data class Ready(
        val pinnedServices: List<PinnedService> = emptyList()
    ) : PinnedState

    data class Error(
        val errorType: String
    ) : PinnedState
}
