package me.cniekirk.ontrackapp.feature.servicedetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import me.cniekirk.ontrackapp.core.designsystem.theme.OnTrackTheme
import me.cniekirk.ontrackapp.core.domain.model.theme.ThemeMode
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun ServiceDetailsRoute(viewModel: ServiceDetailsViewModel) {
    val state = viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->

    }

    ServiceDetailsScreen(state.value)
}

@Composable
private fun ServiceDetailsScreen(state: ServiceDetailsState) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

    }
}

@Preview
@Composable
private fun ServiceDetailsScreenPreview() {
    val state = ServiceDetailsState()

    OnTrackTheme(themeMode = ThemeMode.SYSTEM) {
        Surface {
            ServiceDetailsScreen(state = state)
        }
    }
}
