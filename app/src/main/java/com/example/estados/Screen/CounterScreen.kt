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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.estados.ui.theme.ESTADOSTheme

@Composable
fun CuentaScreen(
    modifier: Modifier = Modifier,
    viewModel: CuentaViewModel = viewModel()
) {
    val montoTexto by viewModel.montoTexto.collectAsState()
    val propinaPct by viewModel.propinaPct.collectAsState()
    val usaOtro by viewModel.usaOtro.collectAsState()
    val sliderVal by viewModel.sliderVal.collectAsState()
    val personas by viewModel.personas.collectAsState()
    val redondear by viewModel.redondear.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Cuenta dividida", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = montoTexto,
            onValueChange = { viewModel.onMontoChange(it) },
            label = { Text("Monto de la cuenta") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            isError = viewModel.avisoMontoInvalido,
            supportingText = {
                if (viewModel.avisoMontoInvalido) {
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
                        onClick = { viewModel.seleccionarPropinaFija(pct) },
                        label = { Text(pct.toString() + " %") }
                    )
                }
                FilterChip(
                    selected = usaOtro,
                    onClick = { viewModel.activarPropinaPersonalizada() },
                    label = { Text("Otro") }
                )
            }

            if (usaOtro) {
                Column {
                    Text(
                        text = "Personalizada: " + propinaPct + "%",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Slider(
                        value = sliderVal,
                        onValueChange = { viewModel.onSliderChange(it) },
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
                    onClick = { viewModel.disminuirPersonas() },
                    modifier = Modifier.size(36.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("–")
                }

                Spacer(modifier = Modifier.width(16.dp))
                Text(text = personas.toString(), style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.width(16.dp))

                OutlinedButton(
                    onClick = { viewModel.aumentarPersonas() },
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
                onCheckedChange = { viewModel.onRedondearChange(it) }
            )
        }

        HorizontalDivider()

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
                    Text(text = "Propina (" + propinaPct + " %)")
                    Text(text = "$" + viewModel.propina)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Total con propina")
                    Text(text = "$" + viewModel.totalConPropina)
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                Text(text = "Cada quien paga", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "$" + viewModel.porPersona,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(text = "entre " + personas + " personas", style = MaterialTheme.typography.bodySmall)

                if (viewModel.sobrante != null) {
                    Text(
                        text = "Sobrante por redondeo: $" + viewModel.sobrante,
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
    ESTADOSTheme {
        CuentaScreen()
    }
}