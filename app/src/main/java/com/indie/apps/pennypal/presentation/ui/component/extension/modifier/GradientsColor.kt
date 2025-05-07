package com.indie.apps.pennypal.presentation.ui.component.extension.modifier

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun linearGradientsBrush(colors: List<Color>): Brush {
    return Brush.linearGradient(
        colors,
        start = Offset(0f, Float.POSITIVE_INFINITY),
        end = Offset(Float.POSITIVE_INFINITY, 30f)
    )
}

fun backgroundGradientsBrush(colors: List<Color>): Color {
    /*return Brush.linearGradient(
        colors,
        start = Offset(200f, 300f),
        end = Offset(800f, 600f)
    )*/

    return colors[1]
}

fun verticalGradientsBrush(colors: List<Color>): Brush {
    return Brush.verticalGradient(
        colors
    )
}
