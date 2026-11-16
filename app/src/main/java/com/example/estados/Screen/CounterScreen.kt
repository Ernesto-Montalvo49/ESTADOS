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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.estados.ui.theme.ESTADOSTheme

@Composable
fun ResenaScreen(
    modifier: Modifier = Modifier,
    viewModel: ResenaViewModel = viewModel()
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Escribe tu reseña", style = MaterialTheme.typography.headlineMedium)

        if (!viewModel.publicada) {
            Text(text = "Restaurante El Mezquite", style = MaterialTheme.typography.titleLarge)
            Text(
                text = "Califica tu experiencia",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            FilaCalificacion(
                titulo = "Comida",
                valor = viewModel.comida,
                descripcion = viewModel.obtenerDescripcion(viewModel.comida),
                onSeleccionar = { viewModel.onComidaChange(it) }
            )

            FilaCalificacion(
                titulo = "Servicio",
                valor = viewModel.servicio,
                descripcion = viewModel.obtenerDescripcion(viewModel.servicio),
                onSeleccionar = { viewModel.onServicioChange(it) }
            )

            FilaCalificacion(
                titulo = "Ambiente",
                valor = viewModel.ambiente,
                descripcion = viewModel.obtenerDescripcion(viewModel.ambiente),
                onSeleccionar = { viewModel.onAmbienteChange(it) }
            )

            if (viewModel.tieneTodas) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Calificación general")
                        Text(
                            text = viewModel.calificacionGeneral.toString() + " +",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            OutlinedTextField(
                value = viewModel.comentario,
                onValueChange = { viewModel.onComentarioChange(it) },
                label = { Text("Comentario (mínimo 20 caracteres)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )
            Text(
                text = viewModel.comentario.length.toString() + "/200",
                style = MaterialTheme.typography.bodySmall
            )

            Text(text = "Etiquetas", style = MaterialTheme.typography.bodyMedium)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                viewModel.listaEtiquetas.forEach { etiqueta ->
                    val seleccionada = etiqueta in viewModel.etiquetas
                    FilterChip(
                        selected = seleccionada,
                        onClick = { viewModel.alternarEtiqueta(etiqueta) },
                        label = { Text(etiqueta) }
                    )
                }
            }

            Button(
                onClick = { viewModel.publicar() },
                enabled = viewModel.puedePublicar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Publicar")
            }

        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = "¡Se publicó la reseña!", style = MaterialTheme.typography.titleLarge)
                    Text(text = "Calificación general: " + viewModel.calificacionGeneral)
                    Text(text = "Comentario: \"" + viewModel.comentario + "\"")
                    Text(
                        text = "Etiquetas: " + if (viewModel.etiquetas.isEmpty()) "Ninguna" else viewModel.etiquetas.joinToString(", ")
                    )

                    Button(
                        onClick = { viewModel.reiniciar() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Escribir otra reseña")
                    }
                }
            }
        }
    }
}

@Composable
fun FilaCalificacion(
    titulo: String,
    valor: Int,
    descripcion: String,
    onSeleccionar: (Int) -> Unit
) {
    Text(text = titulo, style = MaterialTheme.typography.bodyMedium)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            for (i in 1..5) {
                IconButton(onClick = { onSeleccionar(i) }, modifier = Modifier.size(36.dp)) {
                    Text(
                        text = if (i <= valor) "+" else "-",
                        color = if (i <= valor) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
        Text(text = descripcion, color = MaterialTheme.colorScheme.primary)
    }
}

@Preview(showBackground = true)
@Composable
fun ResenaScreenPreview() {
    ESTADOSTheme {
        ResenaScreen()
    }
}