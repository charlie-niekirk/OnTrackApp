package me.cniekirk.ontrackapp.core.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class ServiceDetailRequestData(
    val serviceUid: String,
    val year: String,
    val month: String,
    val day: String,
    val serviceListType: String
)
