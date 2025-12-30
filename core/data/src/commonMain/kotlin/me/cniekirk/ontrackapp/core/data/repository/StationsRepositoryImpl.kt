package me.cniekirk.ontrackapp.core.data.repository

import me.cniekirk.ontrackapp.core.database.StationDatabase
import me.cniekirk.ontrackapp.core.domain.model.Station
import me.cniekirk.ontrackapp.core.domain.repository.StationsRepository
import me.cniekirk.ontrackapp.core.network.service.OpenRailDataService

internal class StationsRepositoryImpl(
    private val openRailDataService: OpenRailDataService,
    private val stationDatabase: StationDatabase
) : StationsRepository {

    override suspend fun updateStations(): Result<List<Station>> {
        return runCatching {
            // Get station list from network
            val stationsListResponse = openRailDataService.getStationList()

            // Clear existing stations and insert new ones in a transaction
            stationDatabase.stationDatabaseQueries.transaction {
                stationDatabase.stationDatabaseQueries.removeAllStations()
                stationsListResponse.stationList.forEach { entry ->
                    stationDatabase.stationDatabaseQueries.insertStation(
                        crs = entry.stationCode,
                        stationName = entry.stationName
                    )
                }
            }

            // Return the domain model list
            stationsListResponse.stationList.map { entry ->
                Station(
                    crs = entry.stationCode,
                    name = entry.stationName
                )
            }
        }
    }

    override suspend fun getStations(forceRefresh: Boolean): Result<List<Station>> {
        return runCatching {
            val dbStations = stationDatabase.stationDatabaseQueries.selectAll().executeAsList()

            if (forceRefresh || dbStations.isEmpty()) {
                // Update will fetch from network and return Result, so unwrap it
                updateStations().getOrThrow()
            } else {
                // Map database entities to domain models
                dbStations.map { dbStation ->
                    Station(
                        crs = dbStation.crs,
                        name = dbStation.stationName
                    )
                }
            }
        }
    }
}