package com.example.estados.Screen

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AforoViewModel : ViewModel() {
    val aforoMax = 50

    private val _personasActuales = MutableStateFlow(32)
    val personasActuales: StateFlow<Int> = _personasActuales.asStateFlow()

    private val _visitasDelDia = MutableStateFlow(118)
    val visitasDelDia: StateFlow<Int> = _visitasDelDia.asStateFlow()

    val porcentaje: Float
        get() = _personasActuales.value.toFloat() / aforoMax

    val porcentajeEntero: Int
        get() = (porcentaje * 100).toInt()

    val puedeEntrar: Boolean
        get() = _personasActuales.value < aforoMax

    val puedeSalir: Boolean
        get() = _personasActuales.value > 0

    val estaLlena: Boolean
        get() = _personasActuales.value >= aforoMax

    val colorEstado: Color
        get() = when {
            porcentajeEntero >= 100 -> Color(0xFFC62828)
            porcentajeEntero >= 70 -> Color(0xFFEF6C00)
            else -> Color(0xFF2E7D32)
        }

    val textoEstado: String
        get() = when {
            estaLlena -> "Sala llena"
            porcentajeEntero >= 70 -> "Aforo alto"
            else -> "Aforo disponible"
        }

    fun registrarEntrada() {
        if (puedeEntrar) {
            _personasActuales.value++
            _visitasDelDia.value++
        }
    }

    fun registrarSalida() {
        if (puedeSalir) {
            _personasActuales.value--
        }
    }

    fun cerrarDia() {
        _personasActuales.value = 0
        _visitasDelDia.value = 0
    }
}