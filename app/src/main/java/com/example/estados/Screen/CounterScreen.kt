package com.example.estados.Screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.estados.ui.theme.ESTADOSTheme

@Composable
fun QuizScreen(
    modifier: Modifier = Modifier,
    viewModel: QuizViewModel = viewModel()
) {
    Box(modifier = modifier.fillMaxSize().padding(16.dp)) {
        when (viewModel.pantalla) {
            Pantalla.INICIO -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Quiz de programación",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = viewModel.nombre,
                        onValueChange = { viewModel.onNombreChange(it) },
                        label = { Text("Nombre del alumno") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.iniciarQuiz() },
                        enabled = viewModel.nombre.trim().length >= 2,
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Text(text = "Comenzar")
                    }
                }
            }

            Pantalla.QUIZ -> {
                val preguntaActual = viewModel.preguntaActual

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(text = "Quiz de programación", style = MaterialTheme.typography.titleLarge)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Pregunta " + (viewModel.indicePregunta + 1) + " de " + viewModel.preguntas.size,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Puntaje: " + viewModel.puntaje,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    LinearProgressIndicator(
                        progress = { viewModel.progreso },
                        modifier = Modifier.fillMaxWidth().height(8.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = preguntaActual.texto,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    preguntaActual.opciones.forEach { opcion ->
                        val esSeleccionada = viewModel.opcionSeleccionada == opcion
                        val esCorrecta = opcion.esCorrecta

                        val colorBorde = when {
                            viewModel.respondida && esCorrecta -> Color(0xFF2E7D32)
                            viewModel.respondida && esSeleccionada && !esCorrecta -> Color(0xFFC62828)
                            else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                        }

                        val colorFondo = when {
                            viewModel.respondida && esCorrecta -> Color(0xFF2E7D32).copy(alpha = 0.1f)
                            viewModel.respondida && esSeleccionada && !esCorrecta -> Color(0xFFC62828).copy(alpha = 0.1f)
                            else -> Color.Transparent
                        }

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = colorFondo,
                            border = BorderStroke(1.dp, colorBorde),
                            onClick = { viewModel.seleccionarOpcion(opcion) }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp).fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    RadioButton(
                                        selected = esSeleccionada,
                                        onClick = { viewModel.seleccionarOpcion(opcion) }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = opcion.texto, style = MaterialTheme.typography.bodyLarge)
                                }

                                if (viewModel.respondida) {
                                    if (esCorrecta) {
                                        Text(
                                            text = "✓",
                                            color = Color(0xFF2E7D32),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp
                                        )
                                    } else if (esSeleccionada) {
                                        Text(
                                            text = "✕",
                                            color = Color(0xFFC62828),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (viewModel.respondida) {
                        val acerto = viewModel.opcionSeleccionada?.esCorrecta == true
                        val mensajeRetro = if (acerto) {
                            "¡Correcto!"
                        } else {
                            "Incorrecto. La respuesta correcta es " + viewModel.textoRespuestaCorrecta + "."
                        }

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = if (acerto) Color(0xFF2E7D32).copy(alpha = 0.15f) else Color(0xFFC62828).copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = mensajeRetro,
                                modifier = Modifier.padding(16.dp),
                                color = if (acerto) Color(0xFF2E7D32) else Color(0xFFC62828),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.confirmarRespuesta() },
                            enabled = viewModel.opcionSeleccionada != null && !viewModel.respondida,
                            modifier = Modifier.weight(1f).height(50.dp)
                        ) {
                            Text("Confirmar")
                        }

                        Button(
                            onClick = { viewModel.siguientePregunta() },
                            enabled = viewModel.respondida,
                            modifier = Modifier.weight(1f).height(50.dp)
                        ) {
                            Text(if (viewModel.esUltimaPregunta) "Ver resultados" else "Siguiente")
                        }
                    }
                }
            }

            Pantalla.RESULTADO -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "¡Muy bien, " + viewModel.nombre.trim() + "!",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = viewModel.puntaje.toString() + " de " + viewModel.preguntas.size,
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = viewModel.mensajeCalificacion,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { viewModel.reintentar() },
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Text(text = "Reintentar")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = { viewModel.cambiarJugador() },
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Text(text = "Cambiar jugador")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizScreenPreview() {
    ESTADOSTheme {
        QuizScreen()
    }
}