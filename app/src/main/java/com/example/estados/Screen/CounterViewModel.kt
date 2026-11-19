package com.example.estados.Screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.ceil

class CuentaViewModel : ViewModel() {
    private val _montoTexto = MutableStateFlow("")
    val montoTexto: StateFlow<String> = _montoTexto.asStateFlow()

    private val _propinaPct = MutableStateFlow(20)
    val propinaPct: StateFlow<Int> = _propinaPct.asStateFlow()

    private val _usaOtro = MutableStateFlow(false)
    val usaOtro: StateFlow<Boolean> = _usaOtro.asStateFlow()

    private val _sliderVal = MutableStateFlow(20f)
    val sliderVal: StateFlow<Float> = _sliderVal.asStateFlow()

    private val _personas = MutableStateFlow(5)
    val personas: StateFlow<Int> = _personas.asStateFlow()

    private val _redondear = MutableStateFlow(false)
    val redondear: StateFlow<Boolean> = _redondear.asStateFlow()

    val monto: Double
        get() = _montoTexto.value.toDoubleOrNull() ?: 0.0

    val avisoMontoInvalido: Boolean
        get() = _montoTexto.value.isNotEmpty() && monto == 0.0

    val propina: Double
        get() = monto * (_propinaPct.value / 100.0)

    val totalConPropina: Double
        get() = monto + propina

    val divisionExacta: Double
        get() = if (_personas.value > 0) totalConPropina / _personas.value else 0.0

    val porPersona: Double
        get() = if (_redondear.value) ceil(divisionExacta) else divisionExacta

    val sobrante: Double?
        get() = if (_redondear.value && _personas.value > 0) {
            (porPersona * _personas.value) - totalConPropina
        } else {
            null
        }

    fun onMontoChange(nuevoMonto: String) {
        _montoTexto.value = nuevoMonto
    }

    fun seleccionarPropinaFija(porcentaje: Int) {
        _usaOtro.value = false
        _propinaPct.value = porcentaje
    }

    fun activarPropinaPersonalizada() {
        _usaOtro.value = true
    }

    fun onSliderChange(nuevoValor: Float) {
        _sliderVal.value = nuevoValor
        _propinaPct.value = nuevoValor.toInt()
    }

    fun aumentarPersonas() {
        if (_personas.value < 20) {
            _personas.value++
        }
    }

    fun disminuirPersonas() {
        if (_personas.value > 1) {
            _personas.value--
        }
    }

    fun onRedondearChange(activo: Boolean) {
        _redondear.value = activo
    }
}