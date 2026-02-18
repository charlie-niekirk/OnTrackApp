package me.cniekirk.ontrackapp.feature.pinned

import me.cniekirk.ontrackapp.core.domain.model.pinned.PinnedService

data class PinnedState(
    val isLoading: Boolean = true,
    val pinnedServices: List<PinnedService> = emptyList()
)
