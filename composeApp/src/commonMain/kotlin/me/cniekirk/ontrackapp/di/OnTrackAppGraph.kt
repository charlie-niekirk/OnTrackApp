package me.cniekirk.ontrackapp.di

import dev.zacsweers.metrox.viewmodel.ViewModelGraph
import me.cniekirk.ontrackapp.core.domain.repository.ThemePreferencesRepository

interface OnTrackAppGraph : ViewModelGraph {
    val themePreferencesRepository: ThemePreferencesRepository
}
