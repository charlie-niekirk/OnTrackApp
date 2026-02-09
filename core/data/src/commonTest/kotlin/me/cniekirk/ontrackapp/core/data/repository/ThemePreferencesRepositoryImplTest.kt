package me.cniekirk.ontrackapp.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import me.cniekirk.ontrackapp.core.datastore.ThemePreferencesDataSource
import me.cniekirk.ontrackapp.core.domain.model.theme.ThemeMode
import kotlin.test.Test
import kotlin.test.assertEquals

class ThemePreferencesRepositoryImplTest {

    @Test
    fun defaultThemeIsSystemAndCanUpdateToDark() = runTest {
        val fakeDataSource = FakeThemePreferencesDataSource()
        val repository = ThemePreferencesRepositoryImpl(fakeDataSource)

        assertEquals(ThemeMode.SYSTEM, repository.themeMode.first())

        repository.setThemeMode(ThemeMode.DARK)

        assertEquals(ThemeMode.DARK, repository.themeMode.first())
    }

    private class FakeThemePreferencesDataSource : ThemePreferencesDataSource {
        private val themeModeState = MutableStateFlow(ThemeMode.SYSTEM)

        override val themeMode: Flow<ThemeMode> = themeModeState

        override suspend fun setThemeMode(themeMode: ThemeMode) {
            themeModeState.value = themeMode
        }
    }
}
