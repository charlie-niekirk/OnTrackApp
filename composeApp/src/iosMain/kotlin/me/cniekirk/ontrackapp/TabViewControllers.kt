package me.cniekirk.ontrackapp

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import me.cniekirk.ontrackapp.di.iosGraph
import me.cniekirk.ontrackapp.navigation.FavouritesTabContent
import me.cniekirk.ontrackapp.navigation.SearchTabNavigation
import me.cniekirk.ontrackapp.navigation.SettingsTabContent
import me.cniekirk.ontrackapp.theme.ThemeHost
import platform.UIKit.UIViewController

fun SearchTabViewController(): UIViewController = ComposeUIViewController {
    CompositionLocalProvider(
        LocalMetroViewModelFactory provides iosGraph.metroViewModelFactory
    ) {
        ThemeHost(themePreferencesRepository = iosGraph.themePreferencesRepository) { _, _ ->
            SearchTabNavigation(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.statusBars)
            )
        }
    }
}

fun FavouritesTabViewController(): UIViewController = ComposeUIViewController {
    CompositionLocalProvider(
        LocalMetroViewModelFactory provides iosGraph.metroViewModelFactory
    ) {
        ThemeHost(themePreferencesRepository = iosGraph.themePreferencesRepository) { _, _ ->
            FavouritesTabContent(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.statusBars)
            )
        }
    }
}

fun SettingsTabViewController(): UIViewController = ComposeUIViewController {
    CompositionLocalProvider(
        LocalMetroViewModelFactory provides iosGraph.metroViewModelFactory
    ) {
        ThemeHost(themePreferencesRepository = iosGraph.themePreferencesRepository) { currentThemeMode, onThemeModeSelected ->
            SettingsTabContent(
                currentThemeMode = currentThemeMode,
                onThemeModeSelected = onThemeModeSelected,
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.statusBars)
            )
        }
    }
}
