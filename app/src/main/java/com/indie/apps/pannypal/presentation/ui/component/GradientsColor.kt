package com.indie.apps.pannypal.presentation.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.indie.apps.pannypal.presentation.ui.theme.MyAppTheme

fun LinearGradientsColor(colors: List<Color>): Brush {
    return Brush.linearGradient(
        colors,
        start = Offset(0f, Float.POSITIVE_INFINITY),
        end = Offset(Float.POSITIVE_INFINITY, 30f)
    )
}

fun VerticalGradientsColor(colors: List<Color>): Brush {
    return Brush.verticalGradient(
        colors
    )
}