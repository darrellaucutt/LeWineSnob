package net.aucutt.lewinesnob.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush

fun createGradient(alpha: Float = 0.7f) = Brush.linearGradient(
    colors = listOf(
        GoldHighlight.copy(alpha = alpha),
        WineBlush.copy(alpha = alpha),
        WineRubyLight.copy(alpha = alpha),
        WineRuby.copy(alpha = alpha),
        WineContainerDark.copy(alpha = alpha),
        WineDeep.copy(alpha = alpha),
        BottleDark.copy(alpha = alpha)
    ),
    start = Offset.Zero,
    end = Offset.Infinite
)