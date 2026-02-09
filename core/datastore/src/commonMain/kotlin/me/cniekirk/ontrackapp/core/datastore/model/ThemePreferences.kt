package me.cniekirk.ontrackapp.core.datastore.model

import kotlinx.serialization.Serializable
import me.cniekirk.ontrackapp.core.domain.model.theme.ThemeMode

@Serializable
data class ThemePreferences(
    val themeMode: ThemeMode = ThemeMode.SYSTEM
)
