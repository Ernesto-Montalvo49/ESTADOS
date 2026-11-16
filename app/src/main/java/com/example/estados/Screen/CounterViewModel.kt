package com.example.estados.Screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

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
    var ingresoTexto by mutableStateOf("4500")
        private set

    val gastos = mutableStateListOf(
        Gasto(1, "Renta mensual", 2000.0, Categoria.RENTA),
        Gasto(2, "Despensa", 650.0, Categoria.COMIDA)
    )

    var conceptoNuevo by mutableStateOf("")
        private set

    var montoNuevo by mutableStateOf("")
        private set

    var catSeleccionada by mutableStateOf(Categoria.COMIDA)
        private set

    val ingreso: Double
        get() = ingresoTexto.toDoubleOrNull() ?: 0.0

    val totalGastado: Double
        get() = gastos.sumOf { it.monto }

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
            val categoriaTop = gastos.groupBy { it.categoria }
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
            val monto = montoNuevo.toDoubleOrNull() ?: 0.0
            return conceptoNuevo.isNotBlank() && monto > 0.0
        }

    fun onIngresoChange(nuevoIngreso: String) {
        ingresoTexto = nuevoIngreso
    }

    fun onConceptoChange(nuevoConcepto: String) {
        conceptoNuevo = nuevoConcepto
    }

    fun onMontoChange(nuevoMonto: String) {
        montoNuevo = nuevoMonto
    }

    fun onCategoriaChange(nuevaCategoria: Categoria) {
        catSeleccionada = nuevaCategoria
    }

    fun agregarGasto() {
        val monto = montoNuevo.toDoubleOrNull() ?: 0.0
        if (formularioValido) {
            val nuevoId = (gastos.maxOfOrNull { it.id } ?: 0) + 1
            gastos.add(
                Gasto(
                    id = nuevoId,
                    concepto = conceptoNuevo.trim(),
                    monto = monto,
                    categoria = catSeleccionada
                )
            )
            conceptoNuevo = ""
            montoNuevo = ""
            catSeleccionada = Categoria.COMIDA
        }
    }

    fun eliminarGasto(gasto: Gasto) {
        gastos.remove(gasto)
    }
}