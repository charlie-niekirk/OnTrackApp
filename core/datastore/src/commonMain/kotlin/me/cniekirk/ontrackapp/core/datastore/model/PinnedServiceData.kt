package me.cniekirk.ontrackapp.core.datastore.model

import kotlinx.serialization.Serializable
import me.cniekirk.ontrackapp.core.domain.model.arguments.TrainStation

@Serializable
data class PinnedServiceData(
    val origin: String,
    val destination: String,
    val targetStation: TrainStation,
    val filterStation: TrainStation?,
    val trainOperatingCompany: String,
    val scheduledArrivalTime: String,
    val serviceDetailRequest: ServiceDetailRequestData
)
