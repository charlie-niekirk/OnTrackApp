package me.cniekirk.ontrackapp.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.cniekirk.ontrackapp.core.data.mapper.toServiceListRequest
import me.cniekirk.ontrackapp.core.datastore.RecentSearchesDataSource
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceListRequest
import me.cniekirk.ontrackapp.core.domain.repository.RecentSearchesRepository

class RecentSearchesRepositoryImpl(
    private val recentSearchesDataSource: RecentSearchesDataSource
) : RecentSearchesRepository {

    override suspend fun cacheRecentSearch(serviceListRequest: ServiceListRequest): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getRecentSearches(): Flow<List<ServiceListRequest>> =
        recentSearchesDataSource.recentSearches.map { it.map { recentSearch -> recentSearch.toServiceListRequest() } }

    override suspend fun deleteRecentSearch(serviceListRequest: ServiceListRequest): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllRecentSearches(): Result<Unit> {
        TODO("Not yet implemented")
    }
}