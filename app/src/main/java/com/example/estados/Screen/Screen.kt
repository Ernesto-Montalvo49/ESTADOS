package com.example.estado.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.estados.ui.theme.ESTADOSTheme

@Composable
fun Micard(modifier: Modifier = Modifier, texto: String) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Text(text = texto, fontSize = 18.sp)
        }
    }
}

@Composable
fun ListaScreen(modifier: Modifier = Modifier) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var listaNombres = remember { mutableStateListOf<String>() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Escribe tu nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                if (nombre.isNotBlank()) {
                    listaNombres.add(nombre)
                    nombre = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(listaNombres) { item ->
                Micard(modifier = Modifier, texto = item)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListaScreenPreview() {
    ESTADOSTheme {
        ListaScreen()
    }
}