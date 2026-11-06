package com.example.estados.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EtiquetaScreen(
    modifier: Modifier,
    nombreInput: String,
    onValueChange: (String) -> Unit,
    onAddClick: () -> Unit,
    nombreMostrado: String
) {
    Column(modifier = modifier.padding(24.dp)) {
        OutlinedTextField(
            value = nombreInput,
            onValueChange = onValueChange,
            label = { Text("Escribe tu nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onAddClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (nombreMostrado.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = nombreMostrado,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EtiquetaScreenPreview() {
    EtiquetaScreen(
        modifier = Modifier.fillMaxSize(),
        nombreInput = "",
        onValueChange = {},
        onAddClick = {},
        nombreMostrado = ""
    )
}