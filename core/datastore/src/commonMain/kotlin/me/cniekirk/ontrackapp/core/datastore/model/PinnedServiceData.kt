package me.cniekirk.ontrackapp.core.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class PinnedServiceData(
    val origin: String,
    val destination: String,
    val trainOperatingCompany: String,
    val scheduledArrivalTime: String,
    val serviceDetailRequest: ServiceDetailRequestData
)
