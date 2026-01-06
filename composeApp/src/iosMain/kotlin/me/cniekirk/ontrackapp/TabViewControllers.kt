package me.cniekirk.ontrackapp

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import dev.zacsweers.metro.createGraph
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import me.cniekirk.ontrackapp.di.IosOnTrackAppGraph
import me.cniekirk.ontrackapp.navigation.FavouritesTabContent
import me.cniekirk.ontrackapp.navigation.SearchTabNavigation
import me.cniekirk.ontrackapp.navigation.SettingsTabContent
import platform.UIKit.UIViewController

private val graph by lazy { createGraph<IosOnTrackAppGraph>() }

fun SearchTabViewController(): UIViewController = ComposeUIViewController {
    CompositionLocalProvider(
        LocalMetroViewModelFactory provides graph.metroViewModelFactory
    ) {
        MaterialTheme {
            SearchTabNavigation(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
            )
        }
    }
}

fun FavouritesTabViewController(): UIViewController = ComposeUIViewController {
    CompositionLocalProvider(
        LocalMetroViewModelFactory provides graph.metroViewModelFactory
    ) {
        MaterialTheme {
            FavouritesTabContent(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
            )
        }
    }
}

fun SettingsTabViewController(): UIViewController = ComposeUIViewController {
    CompositionLocalProvider(
        LocalMetroViewModelFactory provides graph.metroViewModelFactory
    ) {
        MaterialTheme {
            SettingsTabContent(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
            )
        }
    }
}
