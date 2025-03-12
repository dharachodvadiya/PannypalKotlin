package com.indie.apps.pennypal.presentation.ui.component

import android.annotation.SuppressLint
import android.graphics.BlurMaskFilter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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

@SuppressLint("SuspiciousModifierThen")
fun Modifier.shadow(
    color: Color = Color.Black,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
) = then(
    drawBehind {
        drawIntoCanvas { canvas ->
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            if (blurRadius != 0.dp) {
                frameworkPaint.maskFilter = (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL))
            }
            frameworkPaint.color = color.toArgb()

            val leftPixel = offsetX.toPx()
            val topPixel = offsetY.toPx()
            val center = size.width/2
           /* val rightPixel = size.width + topPixel
            val bottomPixel = size.height + leftPixel

            canvas.drawRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                paint = paint,
            )*/

            canvas.drawCircle(
                center = Offset(center+leftPixel, center+topPixel),
                radius = size.width/2,
                paint = paint
            )
        }
    }
)