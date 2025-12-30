package me.cniekirk.ontrackapp.core.network.service

import me.cniekirk.ontrackapp.core.network.model.openraildata.StationListResponse

interface OpenRailDataService {

    suspend fun getStationList(
        currentVersion: String = "1",
    ): StationListResponse
}