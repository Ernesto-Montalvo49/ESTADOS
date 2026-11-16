package com.example.estados.Screen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class Producto(
    val nombre: String,
    val precio: Double,
    val cantidad: Int = 0
)

class CarritoViewModel : ViewModel() {
    val productos = mutableStateListOf(
        Producto("Tortillas 1 kg", 24.00),
        Producto("Leche 1 L", 27.50),
        Producto("Huevo 12 pz", 52.00),
        Producto("Frijol 1 kg", 38.00)
    )

    val totalArticulos: Int
        get() = productos.sumOf { it.cantidad }

    val subtotal: Double
        get() = productos.sumOf { it.precio * it.cantidad }

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
            val pos = productos.indexOf(producto)
            if (pos != -1) {
                productos[pos] = producto.copy(cantidad = producto.cantidad + 1)
            }
        }
    }

    fun decrementar(producto: Producto) {
        if (producto.cantidad > 0) {
            val pos = productos.indexOf(producto)
            if (pos != -1) {
                productos[pos] = producto.copy(cantidad = producto.cantidad - 1)
            }
        }
    }

    fun vaciarCarrito() {
        productos.replaceAll { it.copy(cantidad = 0) }
    }
}