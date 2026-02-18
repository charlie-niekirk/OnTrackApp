package me.cniekirk.ontrackapp.feature.servicelist

import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceDetailRequest
import me.cniekirk.ontrackapp.core.domain.model.arguments.TrainStation

sealed interface ServiceListEffect {

    data object DisplayError : ServiceListEffect

    data class NavigateToServiceDetails(
        val serviceDetailRequest: ServiceDetailRequest,
        val targetStation: TrainStation,
        val filterStation: TrainStation? = null
    ) : ServiceListEffect
}
