package me.cniekirk.ontrackapp

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import dev.zacsweers.metro.createGraph
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import me.cniekirk.ontrackapp.di.IosOnTrackAppGraph
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    val graph = createGraph<IosOnTrackAppGraph>()

    return ComposeUIViewController {
        CompositionLocalProvider(
            LocalMetroViewModelFactory provides graph.metroViewModelFactory
        ) {
            App()
        }
    }
}