package com.example.simplecompose.first

import android.graphics.Paint
import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin

private const val DividerLengthInDegrees = 1.8f

@Composable
fun AnimatedCircle(
    proportions: List<Float>,
    colors: List<Color>,
    modifier: Modifier = Modifier
) {
    val currentState = remember {
        MutableTransitionState(AnimatedCircleProgress.START)
            .apply { targetState = AnimatedCircleProgress.END }
    }
    val stroke = with(LocalDensity.current) { Stroke(5.dp.toPx()) }
    val transition = updateTransition(currentState)
    val angleOffset by transition.animateFloat(
        transitionSpec = {
            tween(
                delayMillis = 100,
                durationMillis = 500,
                easing = LinearOutSlowInEasing
            )
        }, label = ""
    ) { progress ->
        if (progress == AnimatedCircleProgress.START) {
            0f
        } else {
            360f
        }
    }
    val shift by transition.animateFloat(
        transitionSpec = {
            repeatable(4, tween(
                delayMillis = 2000,
                durationMillis = 900,
                easing = CubicBezierEasing(0f, 0f, 0.35f, 1.85f)
            ), repeatMode = RepeatMode.Reverse)

        }, label = ""
    ) { progress ->
        if (progress == AnimatedCircleProgress.START) {
            0f
        } else {
            30f
        }
    }

    Canvas(modifier) {
        val innerRadius = (size.minDimension - stroke.width) / 2
        val halfSize = size / 2.0f
        val topLeft = Offset(
            halfSize.width - innerRadius,
            halfSize.height - innerRadius
        )
        val size = Size(innerRadius * 2, innerRadius * 2)
        var startAngle = 0f
        proportions.forEachIndexed { index, proportion ->
            val sweep = proportion * angleOffset
            drawArc(
                color = colors[index],
                startAngle = startAngle,
                sweepAngle = sweep,
                topLeft = topLeft,
                size = size,
                useCenter = false,
                style = stroke
            )
            val oneOfTen = halfSize.width / 10
            val topP = oneOfTen * 2
            val side = oneOfTen * 3
            val PI2 = 2 * PI
            val  omega = PI2 / (halfSize.width + innerRadius)
            val path = Path().apply {
                for (x in 0..halfSize.width.toInt()) {
                    var y = 30 * sin(x * omega) + 30
                    Log.i("TAGG", "x = $x, y = $y")
                    lineTo(x.toFloat(), y.toFloat())
                }
            }
            drawPath(path, Color.Black, style = Stroke(8f))
            startAngle += sweep
        }
    }
}
private enum class AnimatedCircleProgress { START, END }