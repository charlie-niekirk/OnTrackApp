package me.cniekirk.ontrackapp.core.network.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import me.cniekirk.ontrackapp.core.network.model.realtimetrains.servicedetail.ServiceDetail
import me.cniekirk.ontrackapp.core.network.model.realtimetrains.servicelist.SearchResponse

internal class RealTimeTrainsServiceImpl(
    private val httpClient: HttpClient
) : RealTimeTrainsService {

    override suspend fun getDeparturesToOnDateTime(
        location: String,
        toLocation: String,
        year: String,
        month: String,
        day: String,
        time: String
    ): SearchResponse {
        return httpClient.get("api/v1/json/search/$location/to/$toLocation/$year/$month/$day/$time")
            .body<SearchResponse>()
    }

    override suspend fun getDeparturesOnDateTime(
        location: String,
        year: String,
        month: String,
        day: String,
        time: String
    ): SearchResponse {
        return httpClient.get("api/v1/json/search/$location/$year/$month/$day/$time")
            .body<SearchResponse>()
    }

    override suspend fun getCurrentDepartures(location: String): SearchResponse {
        return httpClient.get("api/v1/json/search/$location")
            .body<SearchResponse>()
    }

    override suspend fun getCurrentDeparturesTo(
        location: String,
        toLocation: String
    ): SearchResponse {
        return httpClient.get("api/v1/json/search/$location/to/$toLocation")
            .body<SearchResponse>()
    }

    override suspend fun getArrivalsFromOnDateTime(
        location: String,
        fromLocation: String,
        year: String,
        month: String,
        day: String,
        time: String
    ): SearchResponse {
        return httpClient.get("api/v1/json/search/$location/from/$fromLocation/$year/$month/$day/$time/arrivals")
            .body<SearchResponse>()
    }

    override suspend fun getArrivalsOnDateTime(
        location: String,
        year: String,
        month: String,
        day: String,
        time: String
    ): SearchResponse {
        return httpClient.get("api/v1/json/search/$location/$year/$month/$day/$time/arrivals")
            .body<SearchResponse>()
    }

    override suspend fun getCurrentArrivals(location: String): SearchResponse {
        return httpClient.get("api/v1/json/search/$location/arrivals")
            .body<SearchResponse>()
    }

    override suspend fun getCurrentArrivalsFrom(
        location: String,
        fromLocation: String
    ): SearchResponse {
        return httpClient.get("api/v1/json/search/$location/from/$fromLocation/arrivals")
            .body<SearchResponse>()
    }

    override suspend fun getServiceDetails(
        serviceUid: String,
        year: String,
        month: String,
        day: String
    ): ServiceDetail {
        return httpClient.get("api/v1/json/service/$serviceUid/$year/$month/$day")
            .body<ServiceDetail>()
    }
}