package com.example.estados.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class Producto(val nombre: String, val precio: Double)

@Composable
fun CarritoScreen(modifier : Modifier = Modifier) {
    val catalogo = remember {
        listOf(
            Producto("Tortillas 1 kg", 24.00),
            Producto("Leche 1 L", 27.50),
            Producto("Huevo 12 pz", 52.00),
            Producto("Frijol 1 kg", 38.00)
        )
    }

    val cantidades = remember { mutableStateListOf(0, 0, 0, 0) }

    val totalArticulos = cantidades.sum()
    val subtotal = catalogo.indices.sumOf { catalogo[it].precio * cantidades[it] }
    val descuento = if (subtotal >= 200.0) subtotal * 0.10 else 0.0
    val subtotalConDescuento = subtotal - descuento
    val envio = when {
        totalArticulos == 0 -> 0.0
        subtotalConDescuento >= 150.0 -> 0.0
        else -> 25.00
    }
    val totalFinal = subtotalConDescuento + envio

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Tienda Don Pepe", style = MaterialTheme.typography.headlineMedium)
                Badge { Text("$totalArticulos") }
            }

            Spacer(modifier = Modifier.height(8.dp))

            catalogo.forEachIndexed { index, producto ->
                ProductoItem(
                    producto = producto,
                    cantidad = cantidades[index],
                    onCantidadChange = { nuevaCantidad ->
                        cantidades[index] = nuevaCantidad
                    }
                )
                Divider()
            }
        }

        ResumenCarrito(
            articulos = totalArticulos,
            subtotal = subtotal,
            descuento = descuento,
            envio = envio,
            total = totalFinal,
            onVaciar = {
                for (i in cantidades.indices) {
                    cantidades[i] = 0
                }
            },
            onConfirmar = {
            }
        )
    }
}

@Composable
fun ProductoItem(
    producto: Producto,
    cantidad: Int,
    onCantidadChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = producto.nombre, style = MaterialTheme.typography.titleMedium)
            Text(text = "$${String.format("%.2f", producto.precio)} c/u", style = MaterialTheme.typography.bodySmall)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(
                onClick = { if (cantidad > 0) onCantidadChange(cantidad - 1) },
                modifier = Modifier.size(36.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("–")
            }

            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "$cantidad", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.width(12.dp))

            OutlinedButton(
                onClick = { if (cantidad < 10) onCantidadChange(cantidad + 1) },
                modifier = Modifier.size(36.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("+")
            }
        }
    }
}

@Composable
fun ResumenCarrito(
    articulos: Int,
    subtotal: Double,
    descuento: Double,
    envio: Double,
    total: Double,
    onVaciar: () -> Unit,
    onConfirmar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilaResumen("Artículos", "$articulos")
            FilaResumen("Subtotal", "$${String.format("%.2f", subtotal)}")

            if (descuento > 0.0) {
                FilaResumen("Descuento 10 %", "-$${String.format("%.2f", descuento)}", esVerde = true)
            }

            val textoEnvio = if (envio == 0.0) "GRATIS" else "$${String.format("%.2f", envio)}"
            FilaResumen("Envio", textoEnvio, esVerde = envio == 0.0)

            Divider(modifier = Modifier.padding(vertical = 4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "TOTAL", style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "$${String.format("%.2f", total)}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onVaciar,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Vaciar")
                }

                Button(
                    onClick = onConfirmar,
                    enabled = articulos > 0,
                    modifier = Modifier.weight(2f)
                ) {
                    Text("Confirmar pedido")
                }
            }
        }
    }
}

@Composable
fun FilaResumen(titulo: String, valor: String, esVerde: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = titulo, style = MaterialTheme.typography.bodyMedium)
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            color = if (esVerde) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCarritoScreen() {
    CarritoScreen()
}