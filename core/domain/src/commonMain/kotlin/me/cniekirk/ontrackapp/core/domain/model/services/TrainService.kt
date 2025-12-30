package me.cniekirk.ontrackapp.core.domain.model.services

import me.cniekirk.ontrackapp.core.domain.model.RunDate

data class TrainService(
    val serviceId: String,
    val runDate: RunDate,
    val origin: String,
    val destination: String,
    val timeStatus: TimeStatus,
    val platform: Platform?,
    val serviceLocation: ServiceLocation?,
    val trainOperatingCompany: String
)