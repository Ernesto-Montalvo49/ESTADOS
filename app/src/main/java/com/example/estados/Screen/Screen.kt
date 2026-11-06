package com.example.estados.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.KeyboardType
import com.example.estados.ui.theme.ESTADOSTheme

@Composable
fun SumaScreen(
    modifier: Modifier,
    numero1: String,
    numero2: String,
    onNum1Changed: (String) -> Unit,
    onNum2Changed: (String) -> Unit,
    resultado: String,
    miclick: () -> Unit
) {
    Column(modifier = modifier.padding(16.dp)) {

        OutlinedTextField(
            value = numero1,
            onValueChange = onNum1Changed,
            label = { Text("Numero 1") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.padding(8.dp))

        OutlinedTextField(
            value = numero2,
            onValueChange = onNum2Changed,
            label = { Text("Numero 2") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.padding(10.dp))

        Button(onClick = { miclick() }) {
            Text("Sumar")
        }

        Spacer(modifier = Modifier.padding(10.dp))

        Text(text = "Resultado: $resultado", fontSize = 40.sp)    }
}

@Preview(showBackground = true)
@Composable
fun ContadorScreenPreview() {
    ESTADOSTheme {
        SumaScreen(
            modifier = Modifier.fillMaxSize(),
            numero1 = "",
            numero2 = "",
            onNum1Changed = {},
            onNum2Changed = {},
            resultado = "",
            miclick = {}
        )
    }
}
