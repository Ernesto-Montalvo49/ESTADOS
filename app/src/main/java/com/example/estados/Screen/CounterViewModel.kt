package com.example.estados.Screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

enum class Pantalla {
    INICIO, QUIZ, RESULTADO
}

data class Opcion(
    val texto: String,
    val esCorrecta: Boolean = false
)

data class Pregunta(
    val texto: String,
    val opciones: List<Opcion>
)

class QuizViewModel : ViewModel() {
    var pantalla by mutableStateOf(Pantalla.INICIO)
        private set

    var nombre by mutableStateOf("")
        private set

    var indicePregunta by mutableStateOf(0)
        private set

    var opcionSeleccionada by mutableStateOf<Opcion?>(null)
        private set

    var respondida by mutableStateOf(false)
        private set

    var puntaje by mutableStateOf(0)
        private set

    val preguntas = listOf(
        Pregunta(
            "¿Qué palabra clave se usa para declarar una variable que puede cambiar en Kotlin?",
            listOf(Opcion("val"), Opcion("var", true), Opcion("fun"), Opcion("const"))
        ),
        Pregunta(
            "¿Cuál es la extensión de los archivos de código fuente en Kotlin?",
            listOf(Opcion(".java"), Opcion(".kt", true), Opcion(".xml"), Opcion(".txt"))
        ),
        Pregunta(
            "¿Qué tipo de dato conviene usar para guardar el valor 3.14?",
            listOf(Opcion("Int"), Opcion("Double", true), Opcion("String"), Opcion("Boolean"))
        ),
        Pregunta(
            "¿Qué componente de Jetpack Compose se usa para organizar elementos en columna?",
            listOf(Opcion("Row"), Opcion("Box"), Opcion("Column", true), Opcion("List"))
        ),
        Pregunta(
            "¿Qué función se usa para recordar un estado en Compose?",
            listOf(Opcion("remember", true), Opcion("save"), Opcion("state"), Opcion("mutable"))
        )
    )

    val preguntaActual: Pregunta
        get() = preguntas[indicePregunta]

    val progreso: Float
        get() = (indicePregunta + 1).toFloat() / preguntas.size

    val esUltimaPregunta: Boolean
        get() = indicePregunta == preguntas.size - 1

    val mensajeCalificacion: String
        get() = when (puntaje) {
            in 0..2 -> "Sigue practicando"
            in 3..4 -> "Buen trabajo"
            else -> "Perfecto"
        }

    val textoRespuestaCorrecta: String
        get() = preguntaActual.opciones.find { it.esCorrecta }?.texto ?: ""

    fun onNombreChange(nuevoNombre: String) {
        nombre = nuevoNombre
    }

    fun iniciarQuiz() {
        if (nombre.trim().length >= 2) {
            pantalla = Pantalla.QUIZ
        }
    }

    fun seleccionarOpcion(opcion: Opcion) {
        if (!respondida) {
            opcionSeleccionada = opcion
        }
    }

    fun confirmarRespuesta() {
        val seleccion = opcionSeleccionada
        if (seleccion != null && !respondida) {
            respondida = true
            if (seleccion.esCorrecta) {
                puntaje++
            }
        }
    }

    fun siguientePregunta() {
        if (!esUltimaPregunta) {
            indicePregunta++
            opcionSeleccionada = null
            respondida = false
        } else {
            pantalla = Pantalla.RESULTADO
        }
    }

    fun reintentar() {
        indicePregunta = 0
        puntaje = 0
        opcionSeleccionada = null
        respondida = false
        pantalla = Pantalla.QUIZ
    }

    fun cambiarJugador() {
        nombre = ""
        indicePregunta = 0
        puntaje = 0
        opcionSeleccionada = null
        respondida = false
        pantalla = Pantalla.INICIO
    }
}