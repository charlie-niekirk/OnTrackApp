package me.cniekirk.ontrackapp

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import me.cniekirk.ontrackapp.navigation.FavouritesTabContent
import me.cniekirk.ontrackapp.navigation.SearchTabNavigation
import me.cniekirk.ontrackapp.navigation.SettingsTabContent
import me.cniekirk.ontrackapp.navigation.Tab

@Composable
fun App() {
    var selectedTab by rememberSaveable<Tab> { mutableStateOf(Tab.Search) }

    MaterialTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing),
            bottomBar = {
                NavigationBar {
                    Tab.entries.forEach { tab ->
                        NavigationBarItem(
                            selected = tab == selectedTab,
                            onClick = { selectedTab = tab },
                            icon = {
                                Icon(
                                    imageVector = tab.toIcon(),
                                    contentDescription = null
                                )
                            },
                            label = { Text(text = tab.title) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            when (selectedTab.id) {
                Tab.Search.id -> SearchTabNavigation(
                    modifier = Modifier.padding(innerPadding)
                )
                Tab.Favourites.id -> FavouritesTabContent(
                    modifier = Modifier.padding(innerPadding)
                )
                Tab.Settings.id -> SettingsTabContent(
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

private fun Tab.toIcon(): ImageVector = when (id) {
    Tab.Search.id -> Icons.Default.Search
    Tab.Favourites.id -> Icons.Default.Favorite
    Tab.Settings.id -> Icons.Default.Settings
    else -> Icons.Default.Search
}
