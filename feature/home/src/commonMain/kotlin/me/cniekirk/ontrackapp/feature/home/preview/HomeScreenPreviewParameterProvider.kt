package me.cniekirk.ontrackapp.feature.home.preview

import me.cniekirk.ontrackapp.feature.home.HomeState
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

internal class HomeScreenPreviewParameterProvider : PreviewParameterProvider<HomeState> {
    override val values: Sequence<HomeState> = sequenceOf(
        HomeState()
    )
}