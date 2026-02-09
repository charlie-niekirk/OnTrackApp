package me.cniekirk.ontrackapp.core.datastore

import kotlinx.coroutines.flow.Flow
import me.cniekirk.ontrackapp.core.domain.model.theme.ThemeMode

interface ThemePreferencesDataSource {

    val themeMode: Flow<ThemeMode>

    suspend fun setThemeMode(themeMode: ThemeMode)
}
