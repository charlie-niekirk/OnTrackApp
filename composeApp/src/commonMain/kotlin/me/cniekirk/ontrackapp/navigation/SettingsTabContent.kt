package me.cniekirk.ontrackapp.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.cniekirk.ontrackapp.core.domain.model.theme.ThemeMode

private data class ThemeModeOption(
    val mode: ThemeMode,
    val title: String,
    val subtitle: String
)

private val THEME_MODE_OPTIONS = listOf(
    ThemeModeOption(
        mode = ThemeMode.SYSTEM,
        title = "System",
        subtitle = "Match the device appearance automatically"
    ),
    ThemeModeOption(
        mode = ThemeMode.LIGHT,
        title = "Light",
        subtitle = "Use the pastel blue light palette"
    ),
    ThemeModeOption(
        mode = ThemeMode.DARK,
        title = "Dark",
        subtitle = "Use the dark palette with pastel blue accents"
    )
)

@Composable
fun SettingsTabContent(
    currentThemeMode: ThemeMode,
    onThemeModeSelected: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Theme",
                    style = MaterialTheme.typography.titleMedium
                )

                THEME_MODE_OPTIONS.forEach { option ->
                    ThemeModeRow(
                        option = option,
                        isSelected = option.mode == currentThemeMode,
                        onThemeModeSelected = onThemeModeSelected
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemeModeRow(
    option: ThemeModeOption,
    isSelected: Boolean,
    onThemeModeSelected: (ThemeMode) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onThemeModeSelected(option.mode) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp)
        ) {
            Text(
                text = option.title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = option.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        RadioButton(
            selected = isSelected,
            onClick = { onThemeModeSelected(option.mode) }
        )
    }
}
