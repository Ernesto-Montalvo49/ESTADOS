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
import com.example.estados.ui.theme.ESTADOSTheme

enum class Pantalla {
    INICIO, QUIZ, RESULTADO
}

data class Pregunta(
    val texto: String,
    val opciones: List<String>,
    val correcta: Int
)

@Composable
fun QuizScreen(modifier: Modifier = Modifier) {
    var pantalla by remember { mutableStateOf(Pantalla.INICIO) }
    var nombre by remember { mutableStateOf("") }
    var indicePregunta by remember { mutableStateOf(0) }
    var seleccionada by remember { mutableStateOf<Int?>(null) }
    var respondida by remember { mutableStateOf(false) }
    var puntaje by remember { mutableStateOf(0) }

    val preguntas = remember {
        listOf(
            Pregunta("¿Qué palabra clave se usa para declarar una variable que puede cambiar en Kotlin?", listOf("val", "var", "fun", "const"), 1),
            Pregunta("¿Cuál es la extensión de los archivos de código fuente en Kotlin?", listOf(".java", ".kt", ".xml", ".txt"), 1),
            Pregunta("¿Qué tipo de dato conviene usar para guardar el valor 3.14?", listOf("Int", "Double", "String", "Boolean"), 1),
            Pregunta("¿Qué componente de Jetpack Compose se usa para organizar elementos en columna?", listOf("Row", "Box", "Column", "List"), 2),
            Pregunta("¿Qué función se usa para recordar un estado en Compose?", listOf("remember", "save", "state", "mutable"), 0)
        )
    }

    Box(modifier = modifier.fillMaxSize().padding(16.dp)) {
        when (pantalla) {
            // --- PANTALLA DE INICIO ---
            Pantalla.INICIO -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Quiz de programación", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre del alumno") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { pantalla = Pantalla.QUIZ },
                        enabled = nombre.trim().length >= 2,
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Text(text = "Comenzar")
                    }
                }
            }

            // --- PANTALLA DE PREGUNTA ---
            Pantalla.QUIZ -> {
                val preguntaActual = preguntas[indicePregunta]
                val progreso = (indicePregunta + 1).toFloat() / preguntas.size

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
                        Text(text = "Pregunta ${indicePregunta + 1} de ${preguntas.size}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Puntaje: $puntaje", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    }

                    LinearProgressIndicator(
                        progress = { progreso },
                        modifier = Modifier.fillMaxWidth().height(8.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = preguntaActual.texto,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    preguntaActual.opciones.forEachIndexed { index, opcion ->
                        val esSeleccionada = seleccionada == index
                        val esCorrecta = index == preguntaActual.correcta

                        val colorBorde = when {
                            respondida && esCorrecta -> Color(0xFF2E7D32)
                            respondida && esSeleccionada && !esCorrecta -> Color(0xFFC62828)
                            else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                        }

                        val colorFondo = when {
                            respondida && esCorrecta -> Color(0xFF2E7D32).copy(alpha = 0.1f)
                            respondida && esSeleccionada && !esCorrecta -> Color(0xFFC62828).copy(alpha = 0.1f)
                            else -> Color.Transparent
                        }

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = colorFondo,
                            border = BorderStroke(1.dp, colorBorde),
                            onClick = {
                                if (!respondida) {
                                    seleccionada = index
                                }
                            }
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
                                        onClick = {
                                            if (!respondida) {
                                                seleccionada = index
                                            }
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = opcion, style = MaterialTheme.typography.bodyLarge)
                                }

                                if (respondida) {
                                    if (esCorrecta) {
                                        Text(text = "✓", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    } else if (esSeleccionada) {
                                        Text(text = "✕", color = Color(0xFFC62828), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    }
                                }
                            }
                        }
                    }

                    if (respondida) {
                        val acerto = seleccionada == preguntaActual.correcta
                        val mensajeRetro = if (acerto) {
                            "¡Correcto!"
                        } else {
                            "Incorrecto. La respuesta correcta es ${preguntaActual.opciones[preguntaActual.correcta]}."
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
                            onClick = {
                                if (seleccionada != null && !respondida) {
                                    respondida = true
                                    if (seleccionada == preguntaActual.correcta) {
                                        puntaje++
                                    }
                                }
                            },
                            enabled = seleccionada != null && !respondida,
                            modifier = Modifier.weight(1f).height(50.dp)
                        ) {
                            Text("Confirmar")
                        }

                        Button(
                            onClick = {
                                if (indicePregunta < preguntas.size - 1) {
                                    indicePregunta++
                                    seleccionada = null
                                    respondida = false
                                } else {
                                    pantalla = Pantalla.RESULTADO
                                }
                            },
                            enabled = respondida,
                            modifier = Modifier.weight(1f).height(50.dp)
                        ) {
                            Text(if (indicePregunta == preguntas.size - 1) "Ver resultados" else "Siguiente")
                        }
                    }
                }
            }

            // --- PANTALLA DE RESULTADOS ---
            Pantalla.RESULTADO -> {
                val mensajeCalificacion = when (puntaje) {
                    in 0..2 -> "Sigue practicando"
                    in 3..4 -> "Buen trabajo"
                    else -> "Perfecto"
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "¡Muy bien, ${nombre.trim()}!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "$puntaje de ${preguntas.size}", style = MaterialTheme.typography.displayMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = mensajeCalificacion, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.secondary)

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            indicePregunta = 0
                            puntaje = 0
                            seleccionada = null
                            respondida = false
                            pantalla = Pantalla.QUIZ
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Text(text = "Reintentar")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = {
                            nombre = ""
                            indicePregunta = 0
                            puntaje = 0
                            seleccionada = null
                            respondida = false
                            pantalla = Pantalla.INICIO
                        },
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