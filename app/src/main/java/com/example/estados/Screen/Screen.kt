package com.example.estados.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class Prioridad {
    ALTA, MEDIA, BAJA
}

enum class Filtro {
    TODAS, PENDIENTES, HECHAS
}

data class Tarea(
    val id: Int,
    val titulo: String,
    val prioridad: Prioridad,
    val hecha: Boolean = false
)

@Composable
fun TareasScreen(modifier: Modifier = Modifier) {
    val tareas = remember {
        mutableStateListOf(
            Tarea(1, "Entregar proyecto POO", Prioridad.ALTA, false),
            Tarea(2, "Leer capítulo 4", Prioridad.BAJA, true),
            Tarea(3, "Taller de cálculo", Prioridad.MEDIA, false),
            Tarea(4, "Preparar exposición", Prioridad.MEDIA, false)
        )
    }

    var textoNueva by remember { mutableStateOf("") }
    var prioridadNueva by remember { mutableStateOf(Prioridad.ALTA) }
    var filtroActual by remember { mutableStateOf(Filtro.TODAS) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    val tareasVisibles = when (filtroActual) {
        Filtro.TODAS -> tareas
        Filtro.PENDIENTES -> tareas.filter { !it.hecha }
        Filtro.HECHAS -> tareas.filter { it.hecha }
    }

    val totalPendientes = tareas.count { !it.hecha }
    val totalTareas = tareas.size
    val hayCompletadas = tareas.any { it.hecha }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "Mis pendientes", style = MaterialTheme.typography.headlineMedium)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = textoNueva,
                    onValueChange = { textoNueva = it },
                    label = { Text("Nueva tarea") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                Button(
                    onClick = {
                        if (textoNueva.isNotBlank()) {
                            val nuevaId = (tareas.maxOfOrNull { it.id } ?: 0) + 1
                            tareas.add(Tarea(id = nuevaId, titulo = textoNueva.trim(), prioridad = prioridadNueva))
                            textoNueva = ""
                        }
                    },
                    modifier = Modifier.size(56.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("+", style = MaterialTheme.typography.titleLarge)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Prioridad", style = MaterialTheme.typography.bodyMedium)
                listOf(Prioridad.ALTA, Prioridad.MEDIA, Prioridad.BAJA).forEach { prio ->
                    FilterChip(
                        selected = prioridadNueva == prio,
                        onClick = { prioridadNueva = prio },
                        label = { Text(prio.name.lowercase().replaceFirstChar { it.uppercase() }) }
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    Filtro.TODAS to "Todas",
                    Filtro.PENDIENTES to "Pendientes",
                    Filtro.HECHAS to "Hechas"
                ).forEach { (filtro, label) ->
                    FilterChip(
                        selected = filtroActual == filtro,
                        onClick = { filtroActual = filtro },
                        label = { Text(label) }
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 4.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(tareasVisibles, key = { it.id }) { tarea ->
                    val indexOriginal = tareas.indexOfFirst { it.id == tarea.id }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Checkbox(
                                checked = tarea.hecha,
                                onCheckedChange = { nuevoEstado ->
                                    if (indexOriginal != -1) {
                                        tareas[indexOriginal] = tareas[indexOriginal].copy(hecha = nuevoEstado)
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = tarea.titulo,
                                    style = MaterialTheme.typography.bodyLarge,
                                    textDecoration = if (tarea.hecha) TextDecoration.LineThrough else null
                                )

                                AssistChip(
                                    onClick = {},
                                    label = {
                                        Text(
                                            tarea.prioridad.name.lowercase().replaceFirstChar { it.uppercase() },
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    },
                                    modifier = Modifier.height(24.dp)
                                )
                            }
                        }

                        IconButton(onClick = {
                            if (indexOriginal != -1) {
                                tareas.removeAt(indexOriginal)
                            }
                        }) {
                            Text(text = "✕", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                    Divider()
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "$totalPendientes pendientes de $totalTareas",
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedButton(
                onClick = { mostrarDialogo = true },
                enabled = hayCompletadas,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Borrar completadas")
            }
        }
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Eliminar tareas") },
            text = { Text("¿Estás seguro de que deseas borrar las ${tareas.count { it.hecha }} tareas completadas?") },
            confirmButton = {
                TextButton(onClick = {
                    tareas.removeAll { it.hecha }
                    mostrarDialogo = false
                }) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCuentaScreen() {
    TareasScreen()
}