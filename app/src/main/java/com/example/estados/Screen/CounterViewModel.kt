package com.example.estados.Screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class Categoria {
    COMIDA, TRANSPORTE, RENTA, OCIO, OTROS;

    fun nombreBonito(): String = when (this) {
        COMIDA -> "Comida"
        TRANSPORTE -> "Transporte"
        RENTA -> "Renta"
        OCIO -> "Ocio"
        OTROS -> "Otros"
    }
}

data class Gasto(
    val id: Int,
    val concepto: String,
    val monto: Double,
    val categoria: Categoria
)

class PresupuestoViewModel : ViewModel() {
    private val _ingresoTexto = MutableStateFlow("4500")
    val ingresoTexto: StateFlow<String> = _ingresoTexto.asStateFlow()

    private val _gastos = MutableStateFlow(
        listOf(
            Gasto(1, "Renta mensual", 2000.0, Categoria.RENTA),
            Gasto(2, "Despensa", 650.0, Categoria.COMIDA)
        )
    )
    val gastos: StateFlow<List<Gasto>> = _gastos.asStateFlow()

    private val _conceptoNuevo = MutableStateFlow("")
    val conceptoNuevo: StateFlow<String> = _conceptoNuevo.asStateFlow()

    private val _montoNuevo = MutableStateFlow("")
    val montoNuevo: StateFlow<String> = _montoNuevo.asStateFlow()

    private val _catSeleccionada = MutableStateFlow(Categoria.COMIDA)
    val catSeleccionada: StateFlow<Categoria> = _catSeleccionada.asStateFlow()

    val ingreso: Double
        get() = _ingresoTexto.value.toDoubleOrNull() ?: 0.0

    val totalGastado: Double
        get() = _gastos.value.sumOf { it.monto }

    val saldoDisponible: Double
        get() = ingreso - totalGastado

    val porcentajeUsado: Float
        get() = if (ingreso > 0) (totalGastado / ingreso).toFloat() else 0.0f

    val porcentajeEntero: Int
        get() = (porcentajeUsado * 100).toInt()

    val excedente: Double
        get() = totalGastado - ingreso

    val infoDetalle: String
        get() {
            val categoriaTop = _gastos.value.groupBy { it.categoria }
                .mapValues { entry -> entry.value.sumOf { it.monto } }
                .maxByOrNull { it.value }

            return if (categoriaTop != null) {
                porcentajeEntero.toString() + " % usado · Mayor gasto: " + categoriaTop.key.nombreBonito() + " ($" + categoriaTop.value.toInt() + ")"
            } else {
                porcentajeEntero.toString() + " % usado"
            }
        }

    val formularioValido: Boolean
        get() {
            val monto = _montoNuevo.value.toDoubleOrNull() ?: 0.0
            return _conceptoNuevo.value.isNotBlank() && monto > 0.0
        }

    fun onIngresoChange(nuevoIngreso: String) {
        _ingresoTexto.value = nuevoIngreso
    }

    fun onConceptoChange(nuevoConcepto: String) {
        _conceptoNuevo.value = nuevoConcepto
    }

    fun onMontoChange(nuevoMonto: String) {
        _montoNuevo.value = nuevoMonto
    }

    fun onCategoriaChange(nuevaCategoria: Categoria) {
        _catSeleccionada.value = nuevaCategoria
    }

    fun agregarGasto() {
        val monto = _montoNuevo.value.toDoubleOrNull() ?: 0.0
        val conceptoLimpio = _conceptoNuevo.value.trim()
        if (formularioValido) {
            val nuevoId = (_gastos.value.maxOfOrNull { it.id } ?: 0) + 1
            _gastos.value = _gastos.value + Gasto(
                id = nuevoId,
                concepto = conceptoLimpio,
                monto = monto,
                categoria = _catSeleccionada.value
            )
            _conceptoNuevo.value = ""
            _montoNuevo.value = ""
            _catSeleccionada.value = Categoria.COMIDA
        }
    }

    fun eliminarGasto(gasto: Gasto) {
        _gastos.value = _gastos.value.filterNot { it.id == gasto.id }
    }
}