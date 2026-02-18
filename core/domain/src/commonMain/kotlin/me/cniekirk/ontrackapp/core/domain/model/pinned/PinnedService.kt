package me.cniekirk.ontrackapp.core.domain.model.pinned

import kotlinx.serialization.Serializable
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceDetailRequest

@Serializable
data class PinnedService(
    val origin: String,
    val destination: String,
    val trainOperatingCompany: String,
    val scheduledArrivalTime: String,
    val serviceDetailRequest: ServiceDetailRequest
)
