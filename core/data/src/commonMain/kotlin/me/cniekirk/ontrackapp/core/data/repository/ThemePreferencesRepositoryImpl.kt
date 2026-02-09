package me.cniekirk.ontrackapp.core.data.repository

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import me.cniekirk.ontrackapp.core.datastore.ThemePreferencesDataSource
import me.cniekirk.ontrackapp.core.domain.model.theme.ThemeMode
import me.cniekirk.ontrackapp.core.domain.repository.ThemePreferencesRepository

@ContributesBinding(AppScope::class)
@Inject
class ThemePreferencesRepositoryImpl(
    private val themePreferencesDataSource: ThemePreferencesDataSource
) : ThemePreferencesRepository {

    override val themeMode: Flow<ThemeMode> = themePreferencesDataSource.themeMode

    override suspend fun setThemeMode(mode: ThemeMode): Result<Unit> {
        return runCatching {
            themePreferencesDataSource.setThemeMode(mode)
        }
    }
}
