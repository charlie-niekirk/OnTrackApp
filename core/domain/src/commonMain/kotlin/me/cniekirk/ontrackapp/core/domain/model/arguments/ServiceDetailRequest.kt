package me.cniekirk.ontrackapp.core.domain.model.arguments

import kotlinx.serialization.Serializable

@Serializable
data class ServiceDetailRequest(
    val serviceUid: String,
    val year: String,
    val month: String,
    val day: String,
    val serviceListType: ServiceListType,
    val targetStation: TrainStation,
    val filterStation: TrainStation? = null
)
