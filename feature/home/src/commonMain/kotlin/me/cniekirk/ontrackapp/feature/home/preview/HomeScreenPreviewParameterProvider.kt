package me.cniekirk.ontrackapp.feature.home.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import me.cniekirk.ontrackapp.feature.home.HomeState

internal class HomeScreenPreviewParameterProvider : PreviewParameterProvider<HomeState> {
    override val values: Sequence<HomeState> = sequenceOf(
        HomeState()
    )
}