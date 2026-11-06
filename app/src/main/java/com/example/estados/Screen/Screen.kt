package com.example.estados.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CuentaScreen(modifier: Modifier = Modifier) {
    var montoTexto by remember { mutableStateOf("") }
    var propinaPct by remember { mutableStateOf(20) }
    var usaOtro by remember { mutableStateOf(false) }
    var sliderVal by remember { mutableStateOf(20f) }
    var personas by remember { mutableStateOf(5) }
    var redondear by remember { mutableStateOf(false) }

    val monto = montoTexto.toDoubleOrNull() ?: 0.0
    val avisoMontoInvalido = montoTexto.isNotEmpty() && monto == 0.0

    val propina = monto * (propinaPct / 100.0)
    val totalConPropina = monto + propina

    val divisionExacta = if (personas > 0) totalConPropina / personas else 0.0
    val porPersona = if (redondear) kotlin.math.ceil(divisionExacta) else divisionExacta

    val sobrante = if (redondear && personas > 0) {
        (porPersona * personas) - totalConPropina
    } else {
        null
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Cuenta dividida", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = montoTexto,
            onValueChange = { montoTexto = it },
            label = { Text("Monto de la cuenta") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            isError = avisoMontoInvalido,
            supportingText = {
                if (avisoMontoInvalido) {
                    Text(text = "Ingresa un número válido", color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Propina", style = MaterialTheme.typography.bodyLarge)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf(10, 15, 20).forEach { pct ->
                    FilterChip(
                        selected = !usaOtro && propinaPct == pct,
                        onClick = {
                            usaOtro = false
                            propinaPct = pct
                        },
                        label = { Text("$pct %") }
                    )
                }
                FilterChip(
                    selected = usaOtro,
                    onClick = { usaOtro = true },
                    label = { Text("Otro") }
                )
            }

            if (usaOtro) {
                Column {
                    Text(text = "Personalizada: ${sliderVal.toInt()}%", style = MaterialTheme.typography.bodySmall)
                    Slider(
                        value = sliderVal,
                        onValueChange = {
                            sliderVal = it
                            propinaPct = it.toInt()
                        },
                        valueRange = 0f..30f
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Personas", style = MaterialTheme.typography.bodyLarge)

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    onClick = { if (personas > 1) personas-- },
                    modifier = Modifier.size(36.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("–")
                }

                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "$personas", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.width(16.dp))

                OutlinedButton(
                    onClick = { if (personas < 20) personas++ },
                    modifier = Modifier.size(36.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("+")
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Redondear por persona", style = MaterialTheme.typography.bodyLarge)
            Switch(
                checked = redondear,
                onCheckedChange = { redondear = it }
            )
        }

        Divider()

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Propina ($propinaPct %)")
                    Text(text = "$${String.format("%.2f", propina)}")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Total con propina")
                    Text(text = "$${String.format("%.2f", totalConPropina)}")
                }

                Divider(modifier = Modifier.padding(vertical = 4.dp))

                Text(text = "Cada quien paga", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "$${String.format("%.2f", porPersona)}",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(text = "entre $personas personas", style = MaterialTheme.typography.bodySmall)

                if (sobrante != null) {
                    Text(
                        text = "Sobrante por redondeo: $${String.format("%.2f", sobrante)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCuentaScreen() {
    CuentaScreen()
}