package me.cniekirk.ontrackapp.core.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class PinnedServices(
    val services: List<PinnedServiceData> = emptyList()
)
