package com.example.estados

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.estados.Screen.EtiquetaScreen
import com.example.estados.ui.theme.ESTADOSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ESTADOSTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var textoInput by remember { mutableStateOf("") }
                    var textoMostrado by remember { mutableStateOf("") }

                    EtiquetaScreen(
                        modifier = Modifier.padding(innerPadding),
                        nombreInput = textoInput,
                        onValueChange = { nuevoTexto -> textoInput = nuevoTexto },
                        onAddClick = { textoMostrado = textoInput },
                        nombreMostrado = textoMostrado
                    )
                }
            }
        }
    }
}