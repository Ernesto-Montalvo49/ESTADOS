package com.example.estados.Screen


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.math.ceil

class CuentaViewModel : ViewModel() {
    var montoTexto by mutableStateOf("")
        private set

    var propinaPct by mutableStateOf(20)
        private set

    var usaOtro by mutableStateOf(false)
        private set

    var sliderVal by mutableStateOf(20f)
        private set

    var personas by mutableStateOf(5)
        private set

    var redondear by mutableStateOf(false)
        private set

    val monto: Double
        get() = montoTexto.toDoubleOrNull() ?: 0.0

    val avisoMontoInvalido: Boolean
        get() = montoTexto.isNotEmpty() && monto == 0.0

    val propina: Double
        get() = monto * (propinaPct / 100.0)

    val totalConPropina: Double
        get() = monto + propina

    val divisionExacta: Double
        get() = if (personas > 0) totalConPropina / personas else 0.0

    val porPersona: Double
        get() = if (redondear) ceil(divisionExacta) else divisionExacta

    val sobrante: Double?
        get() = if (redondear && personas > 0) {
            (porPersona * personas) - totalConPropina
        } else {
            null
        }

    fun onMontoChange(nuevoMonto: String) {
        montoTexto = nuevoMonto
    }

    fun seleccionarPropinaFija(porcentaje: Int) {
        usaOtro = false
        propinaPct = porcentaje
    }

    fun activarPropinaPersonalizada() {
        usaOtro = true
    }

    fun onSliderChange(nuevoValor: Float) {
        sliderVal = nuevoValor
        propinaPct = nuevoValor.toInt()
    }

    fun aumentarPersonas() {
        if (personas < 20) personas++
    }

    fun disminuirPersonas() {
        if (personas > 1) personas--
    }

    fun onRedondearChange(activo: Boolean) {
        redondear = activo
    }
}