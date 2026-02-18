package me.cniekirk.ontrackapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

enum class TopLevelDestinationKey {
    Search,
    Pinned,
    Settings
}

data class TopLevelDestination(
    val title: String,
    val icon: ImageVector
)

val TOP_LEVEL_DESTINATIONS: Map<TopLevelDestinationKey, TopLevelDestination> = linkedMapOf(
    TopLevelDestinationKey.Search to TopLevelDestination(
        title = "Search",
        icon = Icons.Default.Search
    ),
    TopLevelDestinationKey.Pinned to TopLevelDestination(
        title = "Pinned",
        icon = Icons.Default.PushPin
    ),
    TopLevelDestinationKey.Settings to TopLevelDestination(
        title = "Settings",
        icon = Icons.Default.Settings
    )
)

@Composable
fun OnTrackNavigationBar(
    selectedKey: TopLevelDestinationKey,
    onSelectKey: (TopLevelDestinationKey) -> Unit,
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
