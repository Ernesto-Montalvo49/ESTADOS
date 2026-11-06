package com.example.estados.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ContadorScreen(modifier: Modifier, numero: Int, miclick: () -> Unit) {
    Column(modifier = modifier) {
        Text(text = "$numero", fontSize = 80.sp)
        Spacer(modifier = Modifier.padding(10.dp))
        Button(onClick = {
            miclick()
        }) {
            Text("Incrementar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContadorScreenPreview() {
    ContadorScreen(modifier = Modifier.fillMaxSize(),
        numero =0,
        miclick ={}
    )
}