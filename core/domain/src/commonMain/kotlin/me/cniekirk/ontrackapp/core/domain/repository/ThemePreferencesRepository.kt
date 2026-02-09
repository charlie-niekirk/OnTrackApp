package me.cniekirk.ontrackapp.core.domain.repository

import kotlinx.coroutines.flow.Flow
import me.cniekirk.ontrackapp.core.domain.model.theme.ThemeMode

interface ThemePreferencesRepository {

    val themeMode: Flow<ThemeMode>

    suspend fun setThemeMode(mode: ThemeMode): Result<Unit>
}
