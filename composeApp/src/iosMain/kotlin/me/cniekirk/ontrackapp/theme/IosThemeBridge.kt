package me.cniekirk.ontrackapp.theme

import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.cniekirk.ontrackapp.di.iosGraph

interface ThemeModeObserver {
    fun onThemeModeChanged(modeName: String)
}

class ThemeModeBridge {

    private val scope = MainScope()
    private var observerJob: Job? = null

    fun start(observer: ThemeModeObserver) {
        stop()
        observerJob = scope.launch {
            iosGraph.themePreferencesRepository.themeMode.collectLatest { mode ->
                observer.onThemeModeChanged(mode.name)
            }
        }
    }

    fun stop() {
        observerJob?.cancel()
        observerJob = null
    }

    fun dispose() {
        stop()
        scope.cancel()
    }
}
