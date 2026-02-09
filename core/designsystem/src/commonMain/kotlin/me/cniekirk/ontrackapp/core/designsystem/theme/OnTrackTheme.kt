package me.cniekirk.ontrackapp.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import me.cniekirk.ontrackapp.core.domain.model.theme.ThemeMode

internal fun resolveUseDarkTheme(themeMode: ThemeMode, isSystemDarkTheme: Boolean): Boolean {
    return when (themeMode) {
        ThemeMode.SYSTEM -> isSystemDarkTheme
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
}

@Composable
fun OnTrackTheme(
    themeMode: ThemeMode,
    content: @Composable () -> Unit
) {
    val useDarkTheme = resolveUseDarkTheme(themeMode, isSystemInDarkTheme())

    MaterialTheme(
        colorScheme = if (useDarkTheme) DarkColorScheme else LightColorScheme,
        typography = onTrackTypography(),
        content = content
    )
}
