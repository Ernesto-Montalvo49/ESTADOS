package com.example.estados.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.estados.ui.theme.ESTADOSTheme

@Composable
fun CarritoScreen(
    modifier: Modifier = Modifier,
    viewModel: CarritoViewModel = viewModel()
) {
    Column(
        modifier = modifier
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
                Badge { Text("${viewModel.totalArticulos}") }
            }

            Spacer(modifier = Modifier.height(8.dp))

            viewModel.productos.forEach { producto ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = producto.nombre, style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = "$" + producto.precio + " c/u",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedButton(
                            onClick = { viewModel.decrementar(producto) },
                            modifier = Modifier.size(36.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("–")
                        }

                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "${producto.cantidad}", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.width(12.dp))

                        OutlinedButton(
                            onClick = { viewModel.incrementar(producto) },
                            modifier = Modifier.size(36.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("+")
                        }
                    }
                }
                HorizontalDivider()
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Artículos", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "${viewModel.totalArticulos}", style = MaterialTheme.typography.bodyMedium)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Subtotal", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "$" + viewModel.subtotal, style = MaterialTheme.typography.bodyMedium)
                }

                if (viewModel.descuento > 0.0) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Descuento 10 %", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = "-$" + viewModel.descuento,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }

                val textoEnvio = if (viewModel.envio == 0.0) "GRATIS" else "$" + viewModel.envio
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Envío", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = textoEnvio,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (viewModel.envio == 0.0) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurface
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "TOTAL", style = MaterialTheme.typography.titleLarge)
                    Text(
                        text = "$" + viewModel.totalFinal,
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
                        onClick = { viewModel.vaciarCarrito() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Vaciar")
                    }

                    Button(
                        onClick = {},
                        enabled = viewModel.totalArticulos > 0,
                        modifier = Modifier.weight(2f)
                    ) {
                        Text("Confirmar pedido")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCarritoScreen() {
    ESTADOSTheme {
        CarritoScreen()
    }
}