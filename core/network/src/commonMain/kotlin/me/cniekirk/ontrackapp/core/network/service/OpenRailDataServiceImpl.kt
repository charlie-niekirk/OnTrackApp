package me.cniekirk.ontrackapp.core.network.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import me.cniekirk.ontrackapp.core.network.model.openraildata.StationListResponse

private const val API_KEY_HEADER_KEY = "x-apikey"
private const val API_KEY = "q7mYGMISDVnw6RF1hVWm4rfwbTcy7pAYeZ7Arb9EG1kEtoNY"

internal class OpenRailDataServiceImpl(
    private val httpClient: HttpClient
) : OpenRailDataService {

    override suspend fun getStationList(currentVersion: String): StationListResponse {
        return httpClient.get("LDBSVWS/api/ref/20211101/GetStationList/$currentVersion") {
            headers {
                append(API_KEY_HEADER_KEY, API_KEY)
            }
        }.body<StationListResponse>()
    }
}