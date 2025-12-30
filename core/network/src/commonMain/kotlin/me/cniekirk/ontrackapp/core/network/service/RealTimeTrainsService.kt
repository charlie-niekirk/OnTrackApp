package me.cniekirk.ontrackapp.core.network.service

import me.cniekirk.ontrackapp.core.network.model.realtimetrains.servicedetail.ServiceDetail
import me.cniekirk.ontrackapp.core.network.model.realtimetrains.servicelist.SearchResponse

interface RealTimeTrainsService {

    suspend fun getDeparturesToOnDateTime(
        location: String,
        toLocation: String,
        year: String,
        month: String,
        day: String,
        time: String
    ): SearchResponse

    suspend fun getDeparturesOnDateTime(
        location: String,
        year: String,
        month: String,
        day: String,
        time: String
    ): SearchResponse

    suspend fun getCurrentDepartures(
        location: String
    ): SearchResponse

    suspend fun getCurrentDeparturesTo(
        location: String,
        toLocation: String
    ): SearchResponse

    suspend fun getArrivalsFromOnDateTime(
        location: String,
        fromLocation: String,
        year: String,
        month: String,
        day: String,
        time: String
    ): SearchResponse

    suspend fun getArrivalsOnDateTime(
        location: String,
        year: String,
        month: String,
        day: String,
        time: String
    ): SearchResponse

    suspend fun getCurrentArrivals(
        location: String
    ): SearchResponse

    suspend fun getCurrentArrivalsFrom(
        location: String,
        fromLocation: String
    ): SearchResponse

    suspend fun getServiceDetails(
        serviceUid: String,
        year: String,
        month: String,
        day: String
    ): ServiceDetail
}
