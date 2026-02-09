package me.cniekirk.ontrackapp.core.data.mapper

import me.cniekirk.ontrackapp.core.datastore.model.RecentSearch
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceListRequest
import me.cniekirk.ontrackapp.core.domain.model.arguments.TrainStation

internal fun ServiceListRequest.toRecentSearch(): RecentSearch {
    return RecentSearch(
        targetCrs = this.targetStation.crs,
        targetName = this.targetStation.name,
        filterCrs = this.filterStation?.crs,
        filterName = this.filterStation?.name,
        serviceListType = this.serviceListType,
        requestTime = this.requestTime
    )
}

internal fun RecentSearch.toServiceListRequest(): ServiceListRequest {
    val hasFilter = this.filterCrs != null && this.filterName != null

    return ServiceListRequest(
        serviceListType = this.serviceListType,
        requestTime = this.requestTime,
        targetStation = TrainStation(this.targetCrs, this.targetName),
        filterStation = if (hasFilter) TrainStation(this.filterCrs!!, this.filterName!!) else null
    )
}