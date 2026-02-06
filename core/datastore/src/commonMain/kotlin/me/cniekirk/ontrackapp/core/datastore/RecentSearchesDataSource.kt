package me.cniekirk.ontrackapp.core.datastore

import kotlinx.coroutines.flow.Flow
import me.cniekirk.ontrackapp.core.datastore.model.RecentSearch
import me.cniekirk.ontrackapp.core.datastore.model.SearchType
import me.cniekirk.ontrackapp.core.domain.model.arguments.RequestTime

interface RecentSearchesDataSource {

    val recentSearches: Flow<List<RecentSearch>>

    suspend fun addRecentSearch(
        targetCrs: String,
        targetName: String,
        filterCrs: String?,
        filterName: String?,
        searchType: SearchType,
        requestTime: RequestTime
    )

    suspend fun removeRecentSearch(recentSearch: RecentSearch)

    suspend fun clearRecentSearches()
}
