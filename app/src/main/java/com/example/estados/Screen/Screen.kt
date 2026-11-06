package com.example.estados.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ResenaScreen(modifier: Modifier = Modifier) {
    var comida by remember { mutableStateOf(0) }
    var servicio by remember { mutableStateOf(0) }
    var ambiente by remember { mutableStateOf(0) }
    var comentario by remember { mutableStateOf("") }
    var etiquetas by remember { mutableStateOf(setOf<String>()) }
    var publicada by remember { mutableStateOf(false) }

    val tieneTodas = comida > 0 && servicio > 0 && ambiente > 0
    val calificacionGeneral = if (tieneTodas) (comida + servicio + ambiente) / 3.0 else 0.0
    val puedePublicar = tieneTodas && comentario.length >= 20
    val listaEtiquetas = listOf("Familiar", "Económico", "Rápido", "Romántico", "Vegetariano")

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Escribe tu reseña", style = MaterialTheme.typography.headlineMedium)

        if (!publicada) {
            Text(text = "Restaurante El Mezquite", style = MaterialTheme
                .typography.titleLarge)
            Text(text = "Califica tu experiencia", style = MaterialTheme
                .typography.bodyMedium, color = MaterialTheme.colorScheme
                    .onSurfaceVariant)

            Text(text = "Comida", style = MaterialTheme.typography.bodyMedium)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement
                .SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (i in 1..5) {
                        IconButton(onClick = { comida = i }, modifier = Modifier.size(36.dp)) {
                            Text(text = if (i <= comida) "+" else "-", color = if (i <= comida) MaterialTheme
                                .colorScheme.primary else MaterialTheme.colorScheme.outline)
                        }
                    }
                }
                val desc = when (comida) {
                    1 -> "Malo";
                    2 -> "Regular";
                    3 -> "Bueno";
                    4 -> "Muy bueno";
                    5 -> "Excelente"; else -> "Sin calificar" }
                Text(text = desc, color = MaterialTheme.colorScheme.primary)
            }

            Text(text = "Servicio", style = MaterialTheme.typography.bodyMedium)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement
                .SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (i in 1..5) {
                        IconButton(onClick = { servicio = i }, modifier = Modifier.size(36.dp)) {
                            Text(text = if (i <= servicio) "+" else "-",
                                color = if (i <= servicio) MaterialTheme
                                    .colorScheme.primary else MaterialTheme
                                        .colorScheme.outline)
                        }
                    }
                }
                val desc = when (servicio) {
                    1 -> "Malo";
                    2 -> "Regular";
                    3 -> "Bueno";
                    4 -> "Muy bueno";
                    5 -> "Excelente"; else -> "Sin calificar" }
                Text(text = desc, color = MaterialTheme.colorScheme.primary)
            }

            Text(text = "Ambiente", style = MaterialTheme.typography.bodyMedium)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement =
                Arrangement.SpaceBetween, verticalAlignment = Alignment
                    .CenterVertically) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (i in 1..5) {
                        IconButton(onClick = { ambiente = i }, modifier = Modifier.size(36.dp)) {
                            Text(text = if (i <= ambiente) "+" else "-",
                                color = if (i <= ambiente) MaterialTheme
                                    .colorScheme.primary else MaterialTheme
                                        .colorScheme.outline)
                        }
                    }
                }
                val desc = when (ambiente) {
                    1 -> "Malo";
                    2 -> "Regular";
                    3 -> "Bueno";
                    4 -> "Muy bueno";
                    5 -> "Excelente";
                    else -> "Sin calificar" }
                Text(text = desc, color = MaterialTheme.colorScheme.primary)
            }

            if (tieneTodas) {
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults
                    .cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Calificación general")
                        Text(text = String.format("%.1f +", calificacionGeneral),
                            color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            OutlinedTextField(
                value = comentario,
                onValueChange = { if (it.length <= 200) comentario = it },
                label = { Text("Comentario (mínimo 20 caracteres)") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                maxLines = 5
            )
            Text(text = "${comentario.length}/200", style = MaterialTheme.typography.bodySmall)

            Text(text = "Etiquetas", style = MaterialTheme.typography.bodyMedium)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listaEtiquetas.forEach { etiqueta ->
                    val seleccionada = etiqueta in etiquetas
                    FilterChip(
                        selected = seleccionada,
                        onClick = { etiquetas = if (seleccionada) etiquetas - etiqueta else etiquetas + etiqueta },
                        label = { Text(etiqueta) }
                    )
                }
            }

            Button(
                onClick = { publicada = true },
                enabled = puedePublicar,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text(text = "Publicar")
            }

        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = "¡Se publicó la reseña!", style = MaterialTheme.typography.titleLarge)
                    Text(text = "Calificación general: ${String.format("%.1f", calificacionGeneral)}")
                    Text(text = "Comentario: \"$comentario\"")
                    Text(text = "Etiquetas: ${if (etiquetas.isEmpty()) "Ninguna" else etiquetas.joinToString(", ")}")

                    Button(
                        onClick = {
                            comida = 0
                            servicio = 0
                            ambiente = 0
                            comentario = ""
                            etiquetas = emptySet()
                            publicada = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Escribir otra reseña")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResenaScreenPreview() {
    ResenaScreen()
}