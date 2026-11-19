package com.example.estados.Screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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
    private val _ajustes = MutableStateFlow(AjustesLectura())
    val ajustes: StateFlow<AjustesLectura> = _ajustes.asStateFlow()

    fun actualizarTamano(nuevoTamano: Float) {
        _ajustes.value = _ajustes.value.copy(tamano = nuevoTamano)
    }

    fun actualizarInterlineado(nuevoInterlineado: Interlineado) {
        _ajustes.value = _ajustes.value.copy(interlineado = nuevoInterlineado)
    }

    fun actualizarModoOscuro(activo: Boolean) {
        _ajustes.value = _ajustes.value.copy(modoOscuro = activo)
    }

    fun actualizarFuente(usaSerif: Boolean) {
        _ajustes.value = _ajustes.value.copy(usaSerif = usaSerif)
    }

    fun actualizarTexto(nuevoTexto: String) {
        _ajustes.value = _ajustes.value.copy(textoPersonalizado = nuevoTexto)
    }

    fun restablecerValores() {
        _ajustes.value = AjustesLectura()
    }
}