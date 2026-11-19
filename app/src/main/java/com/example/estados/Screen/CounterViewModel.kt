package com.example.estados.Screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ResenaViewModel : ViewModel() {
    private val _comida = MutableStateFlow(0)
    val comida: StateFlow<Int> = _comida.asStateFlow()

    private val _servicio = MutableStateFlow(0)
    val servicio: StateFlow<Int> = _servicio.asStateFlow()

    private val _ambiente = MutableStateFlow(0)
    val ambiente: StateFlow<Int> = _ambiente.asStateFlow()

    private val _comentario = MutableStateFlow("")
    val comentario: StateFlow<String> = _comentario.asStateFlow()

    private val _etiquetas = MutableStateFlow(setOf<String>())
    val etiquetas: StateFlow<Set<String>> = _etiquetas.asStateFlow()

    private val _publicada = MutableStateFlow(false)
    val publicada: StateFlow<Boolean> = _publicada.asStateFlow()

    val listaEtiquetas = listOf("Familiar", "Económico", "Rápido", "Romántico", "Vegetariano")

    val tieneTodas: Boolean
        get() = _comida.value > 0 && _servicio.value > 0 && _ambiente.value > 0

    val calificacionGeneral: Double
        get() = if (tieneTodas) (_comida.value + _servicio.value + _ambiente.value) / 3.0 else 0.0

    val puedePublicar: Boolean
        get() = tieneTodas && _comentario.value.length >= 20

    fun onComidaChange(valor: Int) {
        _comida.value = valor
    }

    fun onServicioChange(valor: Int) {
        _servicio.value = valor
    }

    fun onAmbienteChange(valor: Int) {
        _ambiente.value = valor
    }

    fun onComentarioChange(nuevoComentario: String) {
        if (nuevoComentario.length <= 200) {
            _comentario.value = nuevoComentario
        }
    }

    fun alternarEtiqueta(etiqueta: String) {
        _etiquetas.value = if (etiqueta in _etiquetas.value) {
            _etiquetas.value - etiqueta
        } else {
            _etiquetas.value + etiqueta
        }
    }

    fun publicar() {
        if (puedePublicar) {
            _publicada.value = true
        }
    }

    fun reiniciar() {
        _comida.value = 0
        _servicio.value = 0
        _ambiente.value = 0
        _comentario.value = ""
        _etiquetas.value = emptySet()
        _publicada.value = false
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