package com.example.simplecompose.first

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun FirstScreen(name: String) {
    Text(text = "Hello $name!")
}