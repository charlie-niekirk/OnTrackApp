package me.cniekirk.ontrackapp

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.android.ActivityKey
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory
import me.cniekirk.ontrackapp.core.domain.repository.ThemePreferencesRepository
import me.cniekirk.ontrackapp.navigation.AndroidNavigationRoot

@ContributesIntoMap(AppScope::class, binding<Activity>())
@ActivityKey(MainActivity::class)
@Inject
class MainActivity(
    private val metroViewModelFactory: MetroViewModelFactory,
    private val themePreferencesRepository: ThemePreferencesRepository
) : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            CompositionLocalProvider(
                LocalMetroViewModelFactory provides metroViewModelFactory
            ) {
                AndroidNavigationRoot(themePreferencesRepository = themePreferencesRepository)
            }
        }
    }
}
