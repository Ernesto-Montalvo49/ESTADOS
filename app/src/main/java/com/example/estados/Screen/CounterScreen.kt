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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.estados.ui.theme.ESTADOSTheme

@Composable
fun TareasScreen(
    modifier: Modifier = Modifier,
    viewModel: TareasViewModel = viewModel()
) {
    val tareas by viewModel.tareas.collectAsState()
    val textoNueva by viewModel.textoNueva.collectAsState()
    val prioridadNueva by viewModel.prioridadNueva.collectAsState()
    val filtroActual by viewModel.filtroActual.collectAsState()
    val mostrarDialogo by viewModel.mostrarDialogo.collectAsState()

    val tareasVisibles = when (filtroActual) {
        Filtro.TODAS -> tareas
        Filtro.PENDIENTES -> tareas.filter { !it.hecha }
        Filtro.HECHAS -> tareas.filter { it.hecha }
    }

    val totalPendientes = tareas.count { !it.hecha }
    val totalTareas = tareas.size
    val totalCompletadas = tareas.count { it.hecha }
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
                    onValueChange = { viewModel.onTextoNuevaChange(it) },
                    label = { Text("Nueva tarea") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                Button(
                    onClick = { viewModel.agregarTarea() },
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
                        onClick = { viewModel.onPrioridadNuevaChange(prio) },
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
                        onClick = { viewModel.onFiltroChange(filtro) },
                        label = { Text(label) }
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(tareasVisibles, key = { it.id }) { tarea ->
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
                                    viewModel.cambiarEstadoTarea(tarea, nuevoEstado)
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

                        IconButton(onClick = { viewModel.eliminarTarea(tarea) }) {
                            Text(text = "✕", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                    HorizontalDivider()
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
                text = totalPendientes.toString() + " pendientes de " + totalTareas,
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedButton(
                onClick = { viewModel.abrirDialogoBorrar() },
                enabled = hayCompletadas,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Borrar completadas")
            }
        }
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { viewModel.cerrarDialogoBorrar() },
            title = { Text("Eliminar tareas") },
            text = { Text("¿Estás seguro de que deseas borrar las " + totalCompletadas + " tareas completadas?") },
            confirmButton = {
                TextButton(onClick = { viewModel.borrarCompletadas() }) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.cerrarDialogoBorrar() }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTareasScreen() {
    ESTADOSTheme {
        TareasScreen()
    }
}