package com.indie.apps.pennypal.presentation.ui.component.chart

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.data.module.ChartData
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme

@Composable
fun PieChart(
    data: List<ChartData>,
    radiusOuter: Dp = 80.dp,
    chartBarWidth: Dp = 30.dp,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {

    val totalSum = data.sumOf { it.amount }
    val amountValue = mutableListOf<Double>()

    data.forEachIndexed { index, data ->
        amountValue.add(index, 360.0 * data.amount / totalSum)
    }

    val defaultColor = MyAppTheme.colors.gray3.copy(alpha = 0.3f)

    var lastValue = 0.0

    Box(
        modifier = modifier
            .size(((radiusOuter * 2f) + (chartBarWidth * 2f))),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .size(radiusOuter * 2f)
        ) {
            if (data.isEmpty()) {
                drawCircle(
                    color = defaultColor,
                    style = Stroke(chartBarWidth.toPx(), cap = StrokeCap.Butt)
                )
            } else {
                amountValue.forEachIndexed { index, value ->
                    drawArc(
                        color = data[index].color,
                        lastValue.toFloat(),
                        value.toFloat(),
                        useCenter = false,
                        style = Stroke(chartBarWidth.toPx(), cap = StrokeCap.Butt)
                    )
                    lastValue += value
                }
            }
        }
    }
}