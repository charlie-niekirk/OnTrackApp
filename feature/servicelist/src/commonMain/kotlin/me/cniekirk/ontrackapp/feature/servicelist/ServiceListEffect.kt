package me.cniekirk.ontrackapp.feature.servicelist

import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceDetailRequest

sealed interface ServiceListEffect {

    data object DisplayError : ServiceListEffect

    data class NavigateToServiceDetails(
        val serviceDetailRequest: ServiceDetailRequest
    ) : ServiceListEffect
}
