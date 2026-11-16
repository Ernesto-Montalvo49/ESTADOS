package com.example.estados.Screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CounterViewModel : ViewModel() {
    val aforoMax = 50

    var actuales by mutableStateOf(32)
        private set

    var visitasDelDia by mutableStateOf(118)
        private set

    val porcentaje: Float
        get() = actuales.toFloat() / aforoMax

    val porcentajeTexto: Int
        get() = (porcentaje * 100).toInt()

    fun incrementar() {
        if (actuales < aforoMax) {
            actuales++
            visitasDelDia++
        }
    }

    fun decrementar() {
        if (actuales > 0) {
            actuales--
        }
    }

    fun incrementarGrupo(cantidad: Int = 5) {
        val personasAgregar = (aforoMax - actuales).coerceAtMost(cantidad)
        actuales += personasAgregar
        visitasDelDia += personasAgregar
    }

    fun cerrarDia() {
        actuales = 0
        visitasDelDia = 0
    }
}