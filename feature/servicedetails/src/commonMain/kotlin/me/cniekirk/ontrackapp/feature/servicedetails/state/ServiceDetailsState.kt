package me.cniekirk.ontrackapp.feature.servicedetails.state

import me.cniekirk.ontrackapp.core.domain.model.arguments.TrainStation

sealed interface ServiceDetailsState {

    val targetStation: TrainStation

    val filterStation: TrainStation?

    val isPinned: Boolean

    data class Loading(
        override val targetStation: TrainStation,
        override val filterStation: TrainStation? = null,
        override val isPinned: Boolean = false,
    ) : ServiceDetailsState

    data class Ready(
        override val targetStation: TrainStation,
        override val filterStation: TrainStation? = null,
        override val isPinned: Boolean = false,
        val origin: String,
        val destination: String,
        val timelineRows: List<TimelineRowState> = emptyList(),
        val currentLocation: ServiceCurrentLocation? = null,
        val trainOperatingCompany: String,
        val scheduledArrivalTime: String,
    ) : ServiceDetailsState

    data class Error(
        override val targetStation: TrainStation,
        override val filterStation: TrainStation? = null,
        override val isPinned: Boolean = false,
        val errorType: String,
    ) : ServiceDetailsState
}
