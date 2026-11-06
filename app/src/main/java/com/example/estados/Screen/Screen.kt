package com.example.estados.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.estados.ui.theme.ESTADOSTheme

@Composable
fun RepetirTarjetasScreen(modifier: Modifier = Modifier) {
    var textoNombre by rememberSaveable { mutableStateOf("") }
    var textoCantidad by rememberSaveable { mutableStateOf("") }

    val listaTarjetas = remember { mutableStateListOf<String>() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = textoNombre,
            onValueChange = { textoNombre = it },
            label = { Text("Escribe un nombre o texto") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = textoCantidad,
            onValueChange = { nuevo -> textoCantidad = nuevo.filter { it.isDigit() } },
            label = { Text("N° de Veces") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                if (textoNombre.isNotBlank() && textoCantidad.isNotBlank()) {
                    val cantidad = textoCantidad.toInt()

                    repeat(cantidad) {
                        listaTarjetas.add(textoNombre)
                    }

                    textoNombre = ""
                    textoCantidad = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Generar Tarjetas")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(listaTarjetas) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(modifier = Modifier.padding(16.dp)) {
                        Text(text = item, fontSize = 18.sp)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RepetirTarjetasScreenPreview() {
    ESTADOSTheme {
        RepetirTarjetasScreen(modifier = Modifier.fillMaxSize())
    }
}