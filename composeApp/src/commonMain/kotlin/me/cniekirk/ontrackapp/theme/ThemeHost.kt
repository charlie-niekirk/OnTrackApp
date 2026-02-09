package me.cniekirk.ontrackapp.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import me.cniekirk.ontrackapp.core.designsystem.theme.OnTrackTheme
import me.cniekirk.ontrackapp.core.domain.model.theme.ThemeMode
import me.cniekirk.ontrackapp.core.domain.repository.ThemePreferencesRepository

@Composable
fun ThemeHost(
    themePreferencesRepository: ThemePreferencesRepository,
    content: @Composable (currentThemeMode: ThemeMode, onThemeModeSelected: (ThemeMode) -> Unit) -> Unit
) {
    val currentThemeMode by themePreferencesRepository.themeMode.collectAsState(initial = ThemeMode.SYSTEM)
    val coroutineScope = rememberCoroutineScope()

    OnTrackTheme(themeMode = currentThemeMode) {
        content(
            currentThemeMode,
            { selectedThemeMode ->
                coroutineScope.launch {
                    themePreferencesRepository.setThemeMode(selectedThemeMode)
                }
            }
        )
    }
}
