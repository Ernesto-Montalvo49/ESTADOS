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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.estados.ui.theme.ESTADOSTheme

@Composable
fun PresupuestoScreen(
    modifier: Modifier = Modifier,
    viewModel: PresupuestoViewModel = viewModel()
) {
    val ingresoTexto by viewModel.ingresoTexto.collectAsState()
    val gastos by viewModel.gastos.collectAsState()
    val conceptoNuevo by viewModel.conceptoNuevo.collectAsState()
    val montoNuevo by viewModel.montoNuevo.collectAsState()
    val catSeleccionada by viewModel.catSeleccionada.collectAsState()
    var menuAbierto by remember { mutableStateOf(false) }

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
            onValueChange = { viewModel.onIngresoChange(it) },
            label = { Text("Ingreso mensual") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "Gastado", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = "$" + viewModel.totalGastado.toInt(),
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "Saldo", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = "$" + viewModel.saldoDisponible.toInt(),
                            style = MaterialTheme.typography.headlineSmall,
                            color = if (viewModel.saldoDisponible >= 0) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error
                        )
                    }
                }
                LinearProgressIndicator(
                    progress = { viewModel.porcentajeUsado.coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                )
                Text(text = viewModel.infoDetalle, style = MaterialTheme.typography.bodySmall)
            }
        }

        if (viewModel.porcentajeUsado > 1.0f) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.errorContainer,
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "¡Alerta! Has excedido tu presupuesto por $" + viewModel.excedente.toInt(),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else if (viewModel.porcentajeUsado >= 0.8f) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Cuidado: pasaste el 80 % de tu presupuesto",
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = "Nuevo gasto", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = conceptoNuevo,
                    onValueChange = { viewModel.onConceptoChange(it) },
                    label = { Text("Concepto") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = montoNuevo,
                        onValueChange = { viewModel.onMontoChange(it) },
                        label = { Text("Monto") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedButton(
                            onClick = { menuAbierto = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                                .height(56.dp)
                        ) {
                            Text(text = catSeleccionada.nombreBonito())
                        }
                        DropdownMenu(
                            expanded = menuAbierto,
                            onDismissRequest = { menuAbierto = false }
                        ) {
                            Categoria.entries.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat.nombreBonito()) },
                                    onClick = {
                                        viewModel.onCategoriaChange(cat)
                                        menuAbierto = false
                                    }
                                )
                            }
                        }
                    }
                }
                Button(
                    onClick = { viewModel.agregarGasto() },
                    enabled = viewModel.formularioValido,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Agregar gasto")
                }
            }
        }

        Text(text = "Gastos recientes", style = MaterialTheme.typography.titleMedium)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            gastos.forEach { gasto ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = gasto.concepto, style = MaterialTheme.typography.bodyLarge)
                        Text(
                            text = gasto.categoria.nombreBonito(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "-$" + gasto.monto.toInt(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        IconButton(onClick = { viewModel.eliminarGasto(gasto) }) {
                            Text(text = "X", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
                HorizontalDivider()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PresupuestoScreenPreview() {
    ESTADOSTheme {
        PresupuestoScreen()
    }
}