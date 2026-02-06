package me.cniekirk.ontrackapp.core.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.cniekirk.ontrackapp.core.datastore.model.RecentSearch
import me.cniekirk.ontrackapp.core.datastore.model.RecentSearches
import me.cniekirk.ontrackapp.core.datastore.model.SearchType
import me.cniekirk.ontrackapp.core.domain.model.arguments.RequestTime
import me.cniekirk.ontrackapp.core.domain.model.arguments.ServiceListType

internal class RecentSearchesDataSourceImpl(
    private val dataStore: DataStore<RecentSearches>
) : RecentSearchesDataSource {

    override val recentSearches: Flow<List<RecentSearch>>
        get() = dataStore.data.map { it.searches }

    override suspend fun addRecentSearch(
        targetCrs: String,
        targetName: String,
        filterCrs: String?,
        filterName: String?,
        searchType: SearchType,
        requestTime: RequestTime
    ) {
        dataStore.updateData { currentSearches ->
            val serviceListType = when (searchType) {
                SearchType.DEPARTURES -> ServiceListType.DEPARTURES
                SearchType.ARRIVALS -> ServiceListType.ARRIVALS
            }

            val newSearch = RecentSearch(
                targetCrs = targetCrs,
                targetName = targetName,
                filterCrs = filterCrs,
                filterName = filterName,
                serviceListType = serviceListType,
                requestTime = requestTime
            )

            val existingIndex = currentSearches.searches.indexOfFirst { existing ->
                existing.targetCrs == targetCrs &&
                    existing.filterCrs == filterCrs &&
                    existing.serviceListType == serviceListType
            }

            val updatedSearches = if (existingIndex >= 0) {
                currentSearches.searches.toMutableList().apply {
                    removeAt(existingIndex)
                    add(0, newSearch)
                }
            } else {
                listOf(newSearch) + currentSearches.searches
            }

            currentSearches.copy(
                searches = updatedSearches.take(MAX_RECENT_SEARCHES)
            )
        }
    }

    override suspend fun removeRecentSearch(recentSearch: RecentSearch) {
        dataStore.updateData { currentSearches ->
            currentSearches.copy(
                searches = currentSearches.searches.filter { it != recentSearch }
            )
        }
    }

    override suspend fun clearRecentSearches() {
        dataStore.updateData { RecentSearches() }
    }

    private companion object {
        const val MAX_RECENT_SEARCHES = 10
    }
}

internal expect fun currentTimeMillis(): Long
