package com.example.estados.Screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

enum class Interlineado(val factor: Float) {
    COMPACTO(1.0f),
    NORMAL(1.3f),
    AMPLIO(1.6f)
}

data class AjustesLectura(
    val tamano: Float = 18f,
    val interlineado: Interlineado = Interlineado.NORMAL,
    val modoOscuro: Boolean = false,
    val usaSerif: Boolean = true,
    val textoPersonalizado: String = "Era una noche tranquila en el pueblo cuando las campanas comenzaron a sonar, y nadie supo por qué."
)

class LecturaViewModel : ViewModel() {
    var ajustes by mutableStateOf(AjustesLectura())
        private set

    fun actualizarTamano(nuevoTamano: Float) {
        ajustes = ajustes.copy(tamano = nuevoTamano)
    }

    fun actualizarInterlineado(nuevoInterlineado: Interlineado) {
        ajustes = ajustes.copy(interlineado = nuevoInterlineado)
    }

    fun actualizarModoOscuro(activo: Boolean) {
        ajustes = ajustes.copy(modoOscuro = activo)
    }

    fun actualizarFuente(usaSerif: Boolean) {
        ajustes = ajustes.copy(usaSerif = usaSerif)
    }

    fun actualizarTexto(nuevoTexto: String) {
        ajustes = ajustes.copy(textoPersonalizado = nuevoTexto)
    }

    fun restablecerValores() {
        ajustes = AjustesLectura()
    }
}