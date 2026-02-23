package me.cniekirk.ontrackapp.core.domain.model.pinned

import kotlinx.serialization.Serializable
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceDetailRequest
import me.cniekirk.ontrackapp.core.domain.model.arguments.TrainStation
import me.cniekirk.ontrackapp.core.domain.model.services.TrainService

@Serializable
data class PinnedService(
    val origin: String,
    val destination: String,
    val targetStation: TrainStation,
    val filterStation: TrainStation?,
    val trainOperatingCompany: String,
    val scheduledArrivalTime: String,
    val serviceDetailRequest: ServiceDetailRequest
)
