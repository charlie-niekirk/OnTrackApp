package me.cniekirk.ontrackapp.core.data.repository

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.cniekirk.ontrackapp.core.data.mapper.toServiceListRequest
import me.cniekirk.ontrackapp.core.datastore.RecentSearchesDataSource
import me.cniekirk.ontrackapp.core.datastore.model.SearchType
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceListRequest
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceListType
import me.cniekirk.ontrackapp.core.domain.repository.RecentSearchesRepository

@ContributesBinding(AppScope::class)
@Inject
class RecentSearchesRepositoryImpl(
    private val recentSearchesDataSource: RecentSearchesDataSource
) : RecentSearchesRepository {

    override suspend fun cacheRecentSearch(serviceListRequest: ServiceListRequest): Result<Unit> {
        return runCatching {
            recentSearchesDataSource.addRecentSearch(
                targetCrs = serviceListRequest.targetStation.crs,
                targetName = serviceListRequest.targetStation.name,
                filterCrs = serviceListRequest.filterStation?.crs,
                filterName = serviceListRequest.filterStation?.name,
                searchType = when (serviceListRequest.serviceListType) {
                    ServiceListType.DEPARTURES -> SearchType.DEPARTURES
                    ServiceListType.ARRIVALS -> SearchType.ARRIVALS
                },
                requestTime = serviceListRequest.requestTime
            )
        }
    }

    override val recentSearches: Flow<List<ServiceListRequest>> =
        recentSearchesDataSource.recentSearches.map { it.map { recentSearch -> recentSearch.toServiceListRequest() } }

    override suspend fun deleteRecentSearch(serviceListRequest: ServiceListRequest): Result<Unit> {
        return runCatching {
//            recentSearchesDataSource.removeRecentSearch()
        }
    }

    override suspend fun deleteAllRecentSearches(): Result<Unit> {
        return runCatching {
            recentSearchesDataSource.clearRecentSearches()
        }
    }
}