package com.example.estados.Screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.estados.ui.theme.ESTADOSTheme

@Composable
fun Screen(modifier: Modifier = Modifier) {
    CounterScreen(modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    ESTADOSTheme {
        Screen()
    }
}