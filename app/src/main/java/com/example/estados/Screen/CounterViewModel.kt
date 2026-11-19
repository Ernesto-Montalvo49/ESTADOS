package com.example.estados.Screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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
    private val _pantalla = MutableStateFlow(Pantalla.INICIO)
    val pantalla: StateFlow<Pantalla> = _pantalla.asStateFlow()

    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre.asStateFlow()

    private val _indicePregunta = MutableStateFlow(0)
    val indicePregunta: StateFlow<Int> = _indicePregunta.asStateFlow()

    private val _opcionSeleccionada = MutableStateFlow<Opcion?>(null)
    val opcionSeleccionada: StateFlow<Opcion?> = _opcionSeleccionada.asStateFlow()

    private val _respondida = MutableStateFlow(false)
    val respondida: StateFlow<Boolean> = _respondida.asStateFlow()

    private val _puntaje = MutableStateFlow(0)
    val puntaje: StateFlow<Int> = _puntaje.asStateFlow()

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
        get() = preguntas[_indicePregunta.value]

    val progreso: Float
        get() = (_indicePregunta.value + 1).toFloat() / preguntas.size

    val esUltimaPregunta: Boolean
        get() = _indicePregunta.value == preguntas.size - 1

    val mensajeCalificacion: String
        get() = when (_puntaje.value) {
            in 0..2 -> "Sigue practicando"
            in 3..4 -> "Buen trabajo"
            else -> "Perfecto"
        }

    val textoRespuestaCorrecta: String
        get() = preguntaActual.opciones.find { it.esCorrecta }?.texto ?: ""

    fun onNombreChange(nuevoNombre: String) {
        _nombre.value = nuevoNombre
    }

    fun iniciarQuiz() {
        if (_nombre.value.trim().length >= 2) {
            _pantalla.value = Pantalla.QUIZ
        }
    }

    fun seleccionarOpcion(opcion: Opcion) {
        if (!_respondida.value) {
            _opcionSeleccionada.value = opcion
        }
    }

    fun confirmarRespuesta() {
        val seleccion = _opcionSeleccionada.value
        if (seleccion != null && !_respondida.value) {
            _respondida.value = true
            if (seleccion.esCorrecta) {
                _puntaje.value++
            }
        }
    }

    fun siguientePregunta() {
        if (!esUltimaPregunta) {
            _indicePregunta.value++
            _opcionSeleccionada.value = null
            _respondida.value = false
        } else {
            _pantalla.value = Pantalla.RESULTADO
        }
    }

    fun reintentar() {
        _indicePregunta.value = 0
        _puntaje.value = 0
        _opcionSeleccionada.value = null
        _respondida.value = false
        _pantalla.value = Pantalla.QUIZ
    }

    fun cambiarJugador() {
        _nombre.value = ""
        _indicePregunta.value = 0
        _puntaje.value = 0
        _opcionSeleccionada.value = null
        _respondida.value = false
        _pantalla.value = Pantalla.INICIO
    }
}