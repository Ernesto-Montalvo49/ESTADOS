package com.example.estados.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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

data class Gasto(val id: Int, val concepto: String, val monto: Double, val categoria: Categoria)

@Composable
fun PresupuestoScreen(modifier: Modifier = Modifier) {
    var ingresoTexto by remember { mutableStateOf("4500") }
    val ingreso = ingresoTexto.toDoubleOrNull() ?: 0.0

    val gastos = remember {
        mutableStateListOf(
            Gasto(1, "Renta mensual", 2000.0, Categoria.RENTA),
            Gasto(2, "Despensa", 650.0, Categoria.COMIDA)
        )
    }

    var conceptoNuevo by remember { mutableStateOf("") }
    var montoNuevo by remember { mutableStateOf("") }
    var catSeleccionada by remember { mutableStateOf(Categoria.COMIDA) }
    var menuAbierto by remember { mutableStateOf(false) }

    val totalGastado = gastos.sumOf { it.monto }
    val saldoDisponible = ingreso - totalGastado
    val porcentajeUsado = if (ingreso > 0) (totalGastado / ingreso).toFloat() else 0.0f
    val porcentajeEntero = (porcentajeUsado * 100).toInt()

    val categoriaTop = gastos.groupBy { it.categoria }
        .mapValues { entry -> entry.value.sumOf { it.monto } }
        .maxByOrNull { it.value }

    val montoValido = montoNuevo.toDoubleOrNull() != null && (montoNuevo.toDoubleOrNull() ?: 0.0) > 0.0
    val formularioValido = conceptoNuevo.isNotBlank() && montoValido

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Mi presupuesto", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = ingresoTexto,
            onValueChange = { ingresoTexto = it },
            label = { Text("Ingreso mensual") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text(text = "Gastado", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "$${String.format("%,.0f", totalGastado)}", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.error)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "Saldo", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "$${String.format("%,.0f", saldoDisponible)}", style = MaterialTheme.typography.headlineSmall, color = if (saldoDisponible >= 0) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error)
                    }
                }
                LinearProgressIndicator(progress = { porcentajeUsado.coerceIn(0f, 1f) }, modifier = Modifier.fillMaxWidth().height(8.dp))
                val infoDetalle = if (categoriaTop != null) "$porcentajeEntero % usado · Mayor gasto: ${categoriaTop.key.nombreBonito()} ($${String.format("%,.0f", categoriaTop.value)})" else "$porcentajeEntero % usado"
                Text(text = infoDetalle, style = MaterialTheme.typography.bodySmall)
            }
        }

        if (porcentajeUsado > 1.0f) {
            val excedente = totalGastado - ingreso
            Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.errorContainer, shape = MaterialTheme.shapes.medium) {
                Text(text = "¡Alerta! Has excedido tu presupuesto por $${String.format("%.0f", excedente)}", color = MaterialTheme.colorScheme.onErrorContainer, modifier = Modifier.padding(16.dp))
            }
        } else if (porcentajeUsado >= 0.8f) {
            Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.tertiaryContainer, shape = MaterialTheme.shapes.medium) {
                Text(text = "Cuidado: pasaste el 80 % de tu presupuesto", color = MaterialTheme.colorScheme.onTertiaryContainer, modifier = Modifier.padding(16.dp))
            }
        }

        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(text = "Nuevo gasto", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(value = conceptoNuevo, onValueChange = { conceptoNuevo = it }, label = { Text("Concepto") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = montoNuevo, onValueChange = { montoNuevo = it }, label = { Text("Monto") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true, modifier = Modifier.weight(1f))
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedButton(onClick = { menuAbierto = true }, modifier = Modifier.fillMaxWidth().padding(top = 4.dp).height(56.dp)) {
                            Text(text = catSeleccionada.nombreBonito())
                        }
                        DropdownMenu(expanded = menuAbierto, onDismissRequest = { menuAbierto = false }) {
                            Categoria.values().forEach { cat ->
                                DropdownMenuItem(text = { Text(cat.nombreBonito()) }, onClick = { catSeleccionada = cat; menuAbierto = false })
                            }
                        }
                    }
                }
                Button(
                    onClick = {
                        val monto = montoNuevo.toDoubleOrNull() ?: 0.0
                        if (formularioValido) {
                            val nuevoId = (gastos.maxOfOrNull { it.id } ?: 0) + 1
                            gastos.add(Gasto(id = nuevoId, concepto = conceptoNuevo.trim(), monto = monto, categoria = catSeleccionada))
                            conceptoNuevo = ""
                            montoNuevo = ""
                            catSeleccionada = Categoria.COMIDA
                        }
                    },
                    enabled = formularioValido,
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text(text = "Agregar gasto")
                }
            }
        }

        Text(text = "Gastos recientes", style = MaterialTheme.typography.titleMedium)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            gastos.forEach { gasto ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = gasto.concepto, style = MaterialTheme.typography.bodyLarge)
                        Text(text = gasto.categoria.nombreBonito(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "-$${String.format("%,.0f", gasto.monto)}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
                        IconButton(onClick = { gastos.remove(gasto) }) {
                            Text(text = "X", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
                Divider()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PresupuestoScreenPreview() {
    PresupuestoScreen()
}