package me.cniekirk.ontrackapp.core.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class RecentSearches(
    val searches: List<RecentSearch> = emptyList()
)
