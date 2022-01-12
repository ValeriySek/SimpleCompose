package com.example.simplecompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import com.example.simplecompose.first.AnimatedCircle
import com.example.simplecompose.first.FirstScreen
import com.example.simplecompose.ui.theme.SimpleComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleComposeApp()
        }
    }
}

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
            Box(
                modifier = Modifier
                    .fillMaxSize()
//                .alpha(alpha)
                    .graphicsLayer(alpha = alpha)
                    .background(color = Color.Blue)
            ) {

            }
            AnimatedCircle(proportions = listOf(0.33f, 0.67f), colors = listOf(Color.Cyan, Color.Magenta))
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}