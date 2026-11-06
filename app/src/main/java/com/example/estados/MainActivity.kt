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
import com.example.estados.Screen.SumaScreen
import com.example.estados.ui.theme.ESTADOSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ESTADOSTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var num1 by remember { mutableStateOf("") }
                    var num2 by remember { mutableStateOf("") }
                    var resultado by remember { mutableStateOf("") }

                    SumaScreen(
                        modifier = Modifier.padding(innerPadding),
                        numero1 = num1,
                        numero2 = num2,
                        onNum1Changed = { num1 = it },
                        onNum2Changed = { num2 = it },
                        resultado = resultado,
                        miclick = {
                            val n1 = num1.toIntOrNull() ?: 0
                            val n2 = num2.toIntOrNull() ?: 0
                            resultado = (n1 + n2).toString()
                        }
                    )
                }
            }
        }
    }
}