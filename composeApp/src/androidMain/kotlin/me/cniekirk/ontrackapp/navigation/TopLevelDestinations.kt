package me.cniekirk.ontrackapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import me.cniekirk.ontrackapp.feature.home.navigation.Home

@Serializable
data object Favourites : NavKey

@Serializable
data object Settings : NavKey

data class TopLevelDestination(
    val title: String,
    val icon: ImageVector
)

val TOP_LEVEL_DESTINATIONS: Map<NavKey, TopLevelDestination> = linkedMapOf(
    Home to TopLevelDestination(
        title = "Search",
        icon = Icons.Default.Search
    ),
    Favourites to TopLevelDestination(
        title = "Favourites",
        icon = Icons.Default.Favorite
    ),
    Settings to TopLevelDestination(
        title = "Settings",
        icon = Icons.Default.Settings
    )
)

@Composable
fun OnTrackNavigationBar(
    selectedKey: NavKey,
    onSelectKey: (NavKey) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        TOP_LEVEL_DESTINATIONS.forEach { (destination, data) ->
            NavigationBarItem(
                selected = destination == selectedKey,
                onClick = { onSelectKey(destination) },
                icon = {
                    Icon(
                        imageVector = data.icon,
                        contentDescription = data.title
                    )
                },
                label = { Text(data.title) }
            )
        }
    }
}
