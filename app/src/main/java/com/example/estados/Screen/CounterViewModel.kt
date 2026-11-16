package com.example.estados.Screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ResenaViewModel : ViewModel() {
    var comida by mutableStateOf(0)
        private set

    var servicio by mutableStateOf(0)
        private set

    var ambiente by mutableStateOf(0)
        private set

    var comentario by mutableStateOf("")
        private set

    var etiquetas by mutableStateOf(setOf<String>())
        private set

    var publicada by mutableStateOf(false)
        private set

    val listaEtiquetas = listOf("Familiar", "Económico", "Rápido", "Romántico", "Vegetariano")

    val tieneTodas: Boolean
        get() = comida > 0 && servicio > 0 && ambiente > 0

    val calificacionGeneral: Double
        get() = if (tieneTodas) (comida + servicio + ambiente) / 3.0 else 0.0

    val puedePublicar: Boolean
        get() = tieneTodas && comentario.length >= 20

    fun onComidaChange(valor: Int) {
        comida = valor
    }

    fun onServicioChange(valor: Int) {
        servicio = valor
    }

    fun onAmbienteChange(valor: Int) {
        ambiente = valor
    }

    fun onComentarioChange(nuevoComentario: String) {
        if (nuevoComentario.length <= 200) {
            comentario = nuevoComentario
        }
    }

    fun alternarEtiqueta(etiqueta: String) {
        etiquetas = if (etiqueta in etiquetas) {
            etiquetas - etiqueta
        } else {
            etiquetas + etiqueta
        }
    }

    fun publicar() {
        if (puedePublicar) {
            publicada = true
        }
    }

    fun reiniciar() {
        comida = 0
        servicio = 0
        ambiente = 0
        comentario = ""
        etiquetas = emptySet()
        publicada = false
    }

    fun obtenerDescripcion(valor: Int): String {
        return when (valor) {
            1 -> "Malo"
            2 -> "Regular"
            3 -> "Bueno"
            4 -> "Muy bueno"
            5 -> "Excelente"
            else -> "Sin calificar"
        }
    }
}