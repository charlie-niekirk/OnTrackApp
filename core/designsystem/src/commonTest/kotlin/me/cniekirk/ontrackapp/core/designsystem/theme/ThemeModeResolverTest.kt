package me.cniekirk.ontrackapp.core.designsystem.theme

import me.cniekirk.ontrackapp.core.domain.model.theme.ThemeMode
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ThemeModeResolverTest {

    @Test
    fun systemModeUsesCurrentSystemTheme() {
        assertTrue(resolveUseDarkTheme(ThemeMode.SYSTEM, isSystemDarkTheme = true))
        assertFalse(resolveUseDarkTheme(ThemeMode.SYSTEM, isSystemDarkTheme = false))
    }

    @Test
    fun lightModeAlwaysResolvesToLight() {
        assertFalse(resolveUseDarkTheme(ThemeMode.LIGHT, isSystemDarkTheme = true))
        assertFalse(resolveUseDarkTheme(ThemeMode.LIGHT, isSystemDarkTheme = false))
    }

    @Test
    fun darkModeAlwaysResolvesToDark() {
        assertTrue(resolveUseDarkTheme(ThemeMode.DARK, isSystemDarkTheme = true))
        assertTrue(resolveUseDarkTheme(ThemeMode.DARK, isSystemDarkTheme = false))
    }
}
