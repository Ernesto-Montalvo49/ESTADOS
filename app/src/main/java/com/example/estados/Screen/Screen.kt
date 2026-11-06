package com.example.estados.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.estados.R
import com.example.estados.ui.theme.ESTADOSTheme

@Composable
fun ColorCard(texto: String) {

    val colorResId = when (texto.trim().lowercase()) {
        "Violeta", "purple200" -> R.color.purple_200
        "Morado", "purple500" -> R.color.purple_500
        "Azul", "purple700" -> R.color.purple_700
        "Celeste", "teal200" -> R.color.teal_200
        "Verde", "teal700" -> R.color.teal_700
        else -> android.R.color.darker_gray
    }

    val colorTarjeta = colorResource(id = colorResId)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorTarjeta)
                .padding(16.dp)
        ) {
            Text(
                text = "Color: $texto",
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun ListaColoresScreen(modifier: Modifier = Modifier) {
    var nombreColor by rememberSaveable { mutableStateOf("") }
    val listaColores = remember { mutableStateListOf<String>() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = nombreColor,
            onValueChange = { nombreColor = it },
            label = { Text("Escribe un color (Morado)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                if (nombreColor.isNotBlank()) {
                    listaColores.add(nombreColor)
                    nombreColor = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar Tarjeta de Color")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(listaColores) { colorItem ->
                ColorCard(texto = colorItem)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListaColoresScreenPreview() {
    ESTADOSTheme {
        ListaColoresScreen(modifier = Modifier.fillMaxSize())
    }
}
