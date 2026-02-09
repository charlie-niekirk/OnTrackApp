package me.cniekirk.ontrackapp.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import ontrackapp.core.designsystem.generated.resources.Res
import ontrackapp.core.designsystem.generated.resources.jetbrains_mono_bold
import ontrackapp.core.designsystem.generated.resources.jetbrains_mono_medium
import ontrackapp.core.designsystem.generated.resources.jetbrains_mono_regular
import ontrackapp.core.designsystem.generated.resources.jetbrains_mono_semibold
import org.jetbrains.compose.resources.Font

@Composable
internal fun onTrackTypography(): Typography {
    val retroMonoFamily = FontFamily(
        Font(Res.font.jetbrains_mono_regular, weight = FontWeight.Normal),
        Font(Res.font.jetbrains_mono_medium, weight = FontWeight.Medium),
        Font(Res.font.jetbrains_mono_semibold, weight = FontWeight.SemiBold),
        Font(Res.font.jetbrains_mono_bold, weight = FontWeight.Bold)
    )

    val baseTypography = Typography()

    return Typography(
        displayLarge = baseTypography.displayLarge.retroMono(retroMonoFamily),
        displayMedium = baseTypography.displayMedium.retroMono(retroMonoFamily),
        displaySmall = baseTypography.displaySmall.retroMono(retroMonoFamily),
        headlineLarge = baseTypography.headlineLarge.retroMono(retroMonoFamily),
        headlineMedium = baseTypography.headlineMedium.retroMono(retroMonoFamily),
        headlineSmall = baseTypography.headlineSmall.retroMono(retroMonoFamily),
        titleLarge = baseTypography.titleLarge.retroMono(retroMonoFamily),
        titleMedium = baseTypography.titleMedium.retroMono(retroMonoFamily),
        titleSmall = baseTypography.titleSmall.retroMono(retroMonoFamily),
        bodyLarge = baseTypography.bodyLarge.retroMono(retroMonoFamily),
        bodyMedium = baseTypography.bodyMedium.retroMono(retroMonoFamily),
        bodySmall = baseTypography.bodySmall.retroMono(retroMonoFamily),
        labelLarge = baseTypography.labelLarge.retroMono(retroMonoFamily),
        labelMedium = baseTypography.labelMedium.retroMono(retroMonoFamily),
        labelSmall = baseTypography.labelSmall.retroMono(retroMonoFamily)
    )
}

private fun TextStyle.retroMono(fontFamily: FontFamily): TextStyle = copy(fontFamily = fontFamily)
