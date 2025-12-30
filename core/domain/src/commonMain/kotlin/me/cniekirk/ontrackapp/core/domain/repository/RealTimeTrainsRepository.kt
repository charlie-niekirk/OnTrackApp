package me.cniekirk.ontrackapp.core.domain.repository

import me.cniekirk.ontrackapp.core.domain.model.servicedetails.ServiceDetails
import me.cniekirk.ontrackapp.core.domain.model.services.TrainService

interface RealtimeTrainsRepository {

    suspend fun getDepartureBoardOnDateTime(
        station: String,
        year: String,
        month: String,
        day: String,
        time: String
    ): Result<List<TrainService>>

    suspend fun getDepartureBoardOnDateTimeTo(
        fromStation: String,
        toStation: String,
        year: String,
        month: String,
        day: String,
        time: String
    ): Result<List<TrainService>>

    suspend fun getArrivalBoardOnDateTime(
        station: String,
        year: String,
        month: String,
        day: String,
        time: String
    ): Result<List<TrainService>>

    suspend fun getArrivalBoardOnDateTimeFrom(
        atStation: String,
        fromStation: String,
        year: String,
        month: String,
        day: String,
        time: String
    ): Result<List<TrainService>>

    suspend fun getCurrentDepartureBoard(
        station: String
    ): Result<List<TrainService>>

    suspend fun getCurrentDepartureBoardTo(
        fromStation: String,
        toStation: String
    ): Result<List<TrainService>>

    suspend fun getCurrentArrivalBoard(
        station: String
    ): Result<List<TrainService>>

    suspend fun getCurrentArrivalBoardFrom(
        atStation: String,
        fromStation: String
    ): Result<List<TrainService>>

    suspend fun getServiceDetails(
        serviceUid: String,
        year: String,
        month: String,
        day: String
    ): Result<ServiceDetails>
}