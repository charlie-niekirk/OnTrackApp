package me.cniekirk.ontrackapp.core.domain.repository

import me.cniekirk.ontrackapp.core.domain.model.Station

interface StationsRepository {

    suspend fun updateStations(): Result<List<Station>>

    suspend fun getStations(forceRefresh: Boolean = false): Result<List<Station>>
}