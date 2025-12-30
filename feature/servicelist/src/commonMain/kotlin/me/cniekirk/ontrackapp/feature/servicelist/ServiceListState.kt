package me.cniekirk.ontrackapp.feature.servicelist

import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceListType
import me.cniekirk.ontrackapp.core.domain.model.services.TrainService

data class ServiceListState(
    val isLoading: Boolean = true,
    val trainServiceList: List<TrainService> = emptyList(),
    val filterStation: String? = null,
    val targetStation: String,
    val serviceListType: ServiceListType
)
