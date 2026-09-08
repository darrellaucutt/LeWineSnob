package net.aucutt.lewinesnob.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = WineRuby,
    onPrimary = CreamBright,
    primaryContainer = WineBlush,
    onPrimaryContainer = WineDeep,
    secondary = GoldOchre,
    onSecondary = CreamBright,
    secondaryContainer = GoldCream,
    onSecondaryContainer = GoldDeep,
    tertiary = BottleOlive,
    onTertiary = GoldCream,
    tertiaryContainer = Parchment,
    onTertiaryContainer = BottleDark,
    background = CreamPaper,
    onBackground = Espresso,
    surface = CreamPaper,
    onSurface = Espresso,
    surfaceVariant = Parchment,
    onSurfaceVariant = BottleOlive,
    surfaceTint = WineRuby,
    inverseSurface = Espresso,
    inverseOnSurface = GoldCream,
    inversePrimary = WineRubyLight,
    outline = GoldOchre,
    outlineVariant = GoldRich,
    scrim = Espresso,
    error = WineRuby,
    onError = CreamBright,
    errorContainer = WineBlush,
    onErrorContainer = WineDeep,
    surfaceDim = Parchment,
    surfaceBright = CreamBright,
    surfaceContainerLowest = CreamBright,
    surfaceContainerLow = CreamMuted,
    surfaceContainer = Parchment,
    surfaceContainerHigh = GoldCream,
    surfaceContainerHighest = GoldHighlight,
)

private val DarkColorScheme = darkColorScheme(
    primary = WineRubyLight,
    onPrimary = WineDeep,
    primaryContainer = WineContainerDark,
    onPrimaryContainer = WineBlush,
    secondary = GoldHighlight,
    onSecondary = GoldDeep,
    secondaryContainer = GoldOchre,
    onSecondaryContainer = GoldCream,
    tertiary = Parchment,
    onTertiary = BottleDark,
    tertiaryContainer = BottleOlive,
    onTertiaryContainer = GoldCream,
    background = Espresso,
    onBackground = CreamMuted,
    surface = Espresso,
    onSurface = CreamMuted,
    surfaceVariant = EspressoSoft,
    onSurfaceVariant = GoldCream,
    surfaceTint = WineRubyLight,
    inverseSurface = CreamMuted,
    inverseOnSurface = Espresso,
    inversePrimary = WineRuby,
    outline = GoldRich,
    outlineVariant = BottleOlive,
    scrim = Espresso,
    error = WineRubyLight,
    onError = WineDeep,
    errorContainer = WineContainerDark,
    onErrorContainer = WineBlush,
    surfaceDim = Espresso,
    surfaceBright = BottleDark,
    surfaceContainerLowest = Espresso,
    surfaceContainerLow = EspressoSoft,
    surfaceContainer = BottleDark,
    surfaceContainerHigh = BottleOlive,
    surfaceContainerHighest = GoldOchre,
)

@Composable
fun LeWineSnobTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
