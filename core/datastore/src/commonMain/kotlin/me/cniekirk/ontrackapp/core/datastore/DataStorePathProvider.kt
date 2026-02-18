package me.cniekirk.ontrackapp.core.datastore

import okio.Path

internal const val RECENT_SEARCHES_FILE_NAME = "recent_searches.json"
internal const val PINNED_SERVICES_FILE_NAME = "pinned_services.json"
internal const val THEME_PREFERENCES_FILE_NAME = "theme_preferences.json"

internal expect fun getDataStorePath(fileName: String): Path
