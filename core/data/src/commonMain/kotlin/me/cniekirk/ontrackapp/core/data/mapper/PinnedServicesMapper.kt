package me.cniekirk.ontrackapp.core.data.mapper

import me.cniekirk.ontrackapp.core.datastore.model.PinnedServiceData
import me.cniekirk.ontrackapp.core.datastore.model.ServiceDetailRequestData
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceDetailRequest
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceListType
import me.cniekirk.ontrackapp.core.domain.model.pinned.PinnedService

internal fun PinnedService.toDataModel(): PinnedServiceData {
    return PinnedServiceData(
        origin = origin,
        destination = destination,
        trainOperatingCompany = trainOperatingCompany,
        scheduledArrivalTime = scheduledArrivalTime,
        serviceDetailRequest = serviceDetailRequest.toDataModel()
    )
}

internal fun PinnedServiceData.toDomainModel(): PinnedService {
    return PinnedService(
        origin = origin,
        destination = destination,
        trainOperatingCompany = trainOperatingCompany,
        scheduledArrivalTime = scheduledArrivalTime,
        serviceDetailRequest = serviceDetailRequest.toDomainModel()
    )
}

internal fun ServiceDetailRequest.toDataModel(): ServiceDetailRequestData {
    return ServiceDetailRequestData(
        serviceUid = serviceUid,
        year = year,
        month = month,
        day = day,
        serviceListType = serviceListType.name
    )
}

internal fun ServiceDetailRequestData.toDomainModel(): ServiceDetailRequest {
    return ServiceDetailRequest(
        serviceUid = serviceUid,
        year = year,
        month = month,
        day = day,
        serviceListType = serviceListType.toServiceListType()
    )
}

private fun String.toServiceListType(): ServiceListType {
    return when (this) {
        ServiceListType.ARRIVALS.name -> ServiceListType.ARRIVALS
        else -> ServiceListType.DEPARTURES
    }
}
