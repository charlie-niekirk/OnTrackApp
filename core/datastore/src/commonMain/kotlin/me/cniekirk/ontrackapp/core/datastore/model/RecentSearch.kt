package me.cniekirk.ontrackapp.core.datastore.model

import kotlinx.serialization.Serializable
import me.cniekirk.ontrackapp.core.domain.model.arguments.RequestTime
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceListType

@Serializable
data class RecentSearch(
    val targetCrs: String,
    val targetName: String,
    val filterCrs: String?,
    val filterName: String?,
    val serviceListType: ServiceListType,
    val requestTime: RequestTime
)