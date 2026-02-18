package me.cniekirk.ontrackapp.feature.servicedetails.state

import me.cniekirk.ontrackapp.core.domain.model.arguments.TrainStation

data class ServiceDetailsState(
    val isLoading: Boolean = true,
    val isPinned: Boolean = false,
    val origin: String = "",
    val destination: String = "",
    val timelineRows: List<TimelineRowState> = emptyList(),
    val targetStation: TrainStation? = null,
    val filterStation: TrainStation? = null,
    val currentLocation: ServiceCurrentLocation? = null,
    val trainOperatingCompany: String = "",
    val scheduledArrivalTime: String = "",
)
