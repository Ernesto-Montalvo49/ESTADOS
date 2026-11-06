package com.example.estado

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.estado.Screen.Listas
import com.example.estados.ui.theme.ESTADOSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ESTADOSTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Listas(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}