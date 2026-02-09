package me.cniekirk.ontrackapp.core.datastore.model

import kotlinx.serialization.Serializable

@Serializable
enum class SearchType {
    DEPARTURES,
    ARRIVALS
}