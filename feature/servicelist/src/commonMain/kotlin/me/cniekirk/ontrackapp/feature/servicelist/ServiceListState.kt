package me.cniekirk.ontrackapp.feature.servicelist

import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceListType
import me.cniekirk.ontrackapp.core.domain.model.services.TrainService

sealed interface ServiceListState {

    abstract val serviceListType: ServiceListType

    abstract val targetStation: String

    abstract val filterStation: String?

    data class Loading(
        override val serviceListType: ServiceListType,
        override val targetStation: String,
        override val filterStation: String?
    ) : ServiceListState

    data class Ready(
        override val serviceListType: ServiceListType,
        override val targetStation: String,
        override val filterStation: String?,
        val isRefreshing: Boolean = false,
        val trainServiceList: List<TrainService> = emptyList()
    ) : ServiceListState

    data class Error(
        override val serviceListType: ServiceListType,
        override val targetStation: String,
        override val filterStation: String?,
        val errorType: String
    ) : ServiceListState
}
