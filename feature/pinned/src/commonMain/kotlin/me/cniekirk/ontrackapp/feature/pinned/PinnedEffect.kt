package me.cniekirk.ontrackapp.feature.pinned

import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceDetailRequest

sealed interface PinnedEffect {

    data class NavigateToServiceDetails(
        val serviceDetailRequest: ServiceDetailRequest
    ) : PinnedEffect
}
