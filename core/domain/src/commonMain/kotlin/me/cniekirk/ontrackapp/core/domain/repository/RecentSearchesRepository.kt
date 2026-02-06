package me.cniekirk.ontrackapp.core.domain.repository

import kotlinx.coroutines.flow.Flow
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceListRequest

interface RecentSearchesRepository {

    suspend fun cacheRecentSearch(serviceListRequest: ServiceListRequest): Result<Unit>

    suspend fun getRecentSearches(): Flow<List<ServiceListRequest>>

    suspend fun deleteRecentSearch(serviceListRequest: ServiceListRequest): Result<Unit>

    suspend fun deleteAllRecentSearches(): Result<Unit>
}