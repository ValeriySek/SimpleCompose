package com.example.simplecompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.simplecompose.round_graph.RoundGraph
import com.example.simplecompose.ui.theme.SimpleComposeTheme

@ExperimentalStdlibApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleComposeApp()
        }
    }
}

@ExperimentalStdlibApi
@Composable
fun SimpleComposeApp() {
    val toggleState = false
    val alpha: Float by animateFloatAsState(
        if (toggleState) 1f else 0f,
        animationSpec = tween(5000)
    )
    SimpleComposeTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = Color.White
        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .graphicsLayer(alpha = alpha)
//                    .background(color = Color.Blue)
//            ) {
//
//            }
//            AnimatedCircle(proportions = listOf(0.33f, 0.67f), colors = listOf(Color.Cyan, Color.Magenta))
//            Galaxy(modifier = Modifier
//                .fillMaxSize()
//                .background(color = Color.Black))
//            DotsAndLinesDemo()
            RoundGraph(
                proportions = listOf(0.5f, 0.5f),
                colors = listOf(Color.Cyan, Color.Magenta)
            )
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}