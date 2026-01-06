package me.cniekirk.ontrackapp.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Tab(
    val id: String,
    val title: String,
    val iconName: String
) {
    @Serializable
    data object Search : Tab(
        id = "search",
        title = "Search",
        iconName = "magnifyingglass"
    )

    @Serializable
    data object Favourites : Tab(
        id = "favourites",
        title = "Favourites",
        iconName = "heart.fill"
    )

    @Serializable
    data object Settings : Tab(
        id = "settings",
        title = "Settings",
        iconName = "gearshape.fill"
    )

    companion object {
        val entries: List<Tab> = listOf(Search, Favourites, Settings)

        fun fromId(id: String): Tab = when (id) {
            "search" -> Search
            "favourites" -> Favourites
            "settings" -> Settings
            else -> Search
        }
    }
}
