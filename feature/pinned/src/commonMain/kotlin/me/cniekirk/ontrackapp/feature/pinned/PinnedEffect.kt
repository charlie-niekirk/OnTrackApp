package me.cniekirk.ontrackapp.feature.pinned

import me.cniekirk.ontrackapp.core.domain.model.pinned.PinnedService

sealed interface PinnedEffect {

    data class NavigateToServiceDetails(
        val pinnedService: PinnedService
    ) : PinnedEffect
}
