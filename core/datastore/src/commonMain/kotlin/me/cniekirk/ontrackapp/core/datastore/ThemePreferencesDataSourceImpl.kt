package me.cniekirk.ontrackapp.core.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.cniekirk.ontrackapp.core.datastore.model.ThemePreferences
import me.cniekirk.ontrackapp.core.domain.model.theme.ThemeMode

internal class ThemePreferencesDataSourceImpl(
    private val dataStore: DataStore<ThemePreferences>
) : ThemePreferencesDataSource {

    override val themeMode: Flow<ThemeMode>
        get() = dataStore.data.map { it.themeMode }

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        dataStore.updateData { currentPreferences ->
            currentPreferences.copy(themeMode = themeMode)
        }
    }
}
