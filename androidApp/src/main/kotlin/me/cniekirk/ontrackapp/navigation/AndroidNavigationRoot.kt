package me.cniekirk.ontrackapp.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import me.cniekirk.ontrackapp.core.domain.repository.ThemePreferencesRepository
import me.cniekirk.ontrackapp.theme.ThemeHost

@Composable
fun AndroidNavigationRoot(
    themePreferencesRepository: ThemePreferencesRepository,
    modifier: Modifier = Modifier
) {
    var selectedTab by rememberSaveable { mutableStateOf(TopLevelDestinationKey.Search) }
    val tabStateHolder = rememberSaveableStateHolder()

    BackHandler(enabled = selectedTab != TopLevelDestinationKey.Search) {
        selectedTab = TopLevelDestinationKey.Search
    }

    ThemeHost(themePreferencesRepository = themePreferencesRepository) { currentThemeMode, onThemeModeSelected ->
        Scaffold(
            modifier = modifier,
            bottomBar = {
                OnTrackNavigationBar(
                    selectedKey = selectedTab,
                    onSelectKey = { selectedTab = it }
                )
            }
        ) { innerPadding ->
            tabStateHolder.SaveableStateProvider(selectedTab) {
                when (selectedTab) {
                    TopLevelDestinationKey.Search -> SearchTabNavigation(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .consumeWindowInsets(innerPadding)
                    )

                    TopLevelDestinationKey.Pinned -> PinnedTabContent(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .consumeWindowInsets(innerPadding)
                    )

                    TopLevelDestinationKey.Settings -> SettingsTabContent(
                        currentThemeMode = currentThemeMode,
                        onThemeModeSelected = onThemeModeSelected,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .consumeWindowInsets(innerPadding)
                    )
                }
            }
        }
    }
}
