package com.example.estados.Screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Producto(
    val nombre: String,
    val precio: Double,
    val cantidad: Int = 0
)

class CarritoViewModel : ViewModel() {
    private val _productos = MutableStateFlow(
        listOf(
            Producto("Tortillas 1 kg", 24.00),
            Producto("Leche 1 L", 27.50),
            Producto("Huevo 12 pz", 52.00),
            Producto("Frijol 1 kg", 38.00)
        )
    )
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    val totalArticulos: Int
        get() = _productos.value.sumOf { it.cantidad }

    val subtotal: Double
        get() = _productos.value.sumOf { it.precio * it.cantidad }

    val descuento: Double
        get() = if (subtotal >= 200.0) subtotal * 0.10 else 0.0

    val subtotalConDescuento: Double
        get() = subtotal - descuento

    val envio: Double
        get() = when {
            totalArticulos == 0 -> 0.0
            subtotalConDescuento >= 150.0 -> 0.0
            else -> 25.00
        }

    val totalFinal: Double
        get() = subtotalConDescuento + envio

    fun incrementar(producto: Producto) {
        if (producto.cantidad < 10) {
            _productos.value = _productos.value.map { actual ->
                if (actual == producto) actual.copy(cantidad = actual.cantidad + 1) else actual
            }
        }
    }

    fun decrementar(producto: Producto) {
        if (producto.cantidad > 0) {
            _productos.value = _productos.value.map { actual ->
                if (actual == producto) actual.copy(cantidad = actual.cantidad - 1) else actual
            }
        }
    }

    fun vaciarCarrito() {
        _productos.value = _productos.value.map { it.copy(cantidad = 0) }
    }
}