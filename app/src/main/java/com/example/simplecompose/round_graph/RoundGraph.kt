package com.example.simplecompose.round_graph

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun RoundGraph(
    proportions: List<Float>,
    colors: List<Color>,
    modifier: Modifier = Modifier
) {

    val stroke = with(LocalDensity.current) { Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round)}

    Canvas(modifier) {
        val innerRadius = (size.minDimension - stroke.width) / 2
        val halfSize = size / 2.0f
        val topLeft = Offset(
            halfSize.width - innerRadius,
            halfSize.height - innerRadius
        )
        val size = Size(innerRadius * 2, innerRadius * 2)
        proportions.forEachIndexed { index, fl ->
            drawArc(
                color = Color.LightGray,
                startAngle = 90f,
                sweepAngle = 270f,
                topLeft = topLeft,
                size = size,
                useCenter = false,
                style = stroke
            )
            drawArc(
                color = colors[index],
                startAngle = 90f,
                sweepAngle = 200f,
                topLeft = topLeft,
                size = size,
                useCenter = false,
                style = stroke
            )
        }
    }

}