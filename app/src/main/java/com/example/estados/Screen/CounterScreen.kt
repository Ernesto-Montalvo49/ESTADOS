package com.example.estados.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.estados.ui.theme.ESTADOSTheme

@Composable
fun LecturaScreen(
    modifier: Modifier = Modifier,
    viewModel: LecturaViewModel = viewModel()
) {
    val ajustes = viewModel.ajustes

    val backgroundColor = if (ajustes.modoOscuro) Color.Black else Color.White
    val textColor = if (ajustes.modoOscuro) Color.White else Color.Black

    Box(
        modifier = modifier
            .background(backgroundColor)
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Ajustes de lectura",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )

                ControlesLectura(
                    ajustes = ajustes,
                    viewModel = viewModel,
                    textColor = textColor
                )

                VistaPrevia(
                    ajustes = ajustes,
                    onTextChange = { viewModel.actualizarTexto(it) }
                )
            }

            Button(
                onClick = { viewModel.restablecerValores() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Restablecer")
            }
        }
    }
}

@Composable
fun ControlesLectura(
    ajustes: AjustesLectura,
    viewModel: LecturaViewModel,
    textColor: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Tamaño de letra", color = textColor)
            Text(
                text = "${ajustes.tamano.toInt()} sp",
                color = Color(0xFF6750A4),
                fontWeight = FontWeight.Bold
            )
        }
        Slider(
            value = ajustes.tamano,
            onValueChange = { viewModel.actualizarTamano(it) },
            valueRange = 14f..32f
        )

        Text(text = "Interlineado", color = textColor)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Interlineado.entries.forEach { tipo ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        viewModel.actualizarInterlineado(tipo)
                    }
                ) {
                    RadioButton(
                        selected = ajustes.interlineado == tipo,
                        onClick = { viewModel.actualizarInterlineado(tipo) }
                    )
                    Text(
                        text = tipo.name.lowercase().replaceFirstChar { it.uppercase() },
                        color = textColor,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Modo oscuro", color = textColor)
            Switch(
                checked = ajustes.modoOscuro,
                onCheckedChange = { viewModel.actualizarModoOscuro(it) }
            )
        }

        Text(text = "Fuente", color = textColor)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = ajustes.usaSerif,
                onClick = { viewModel.actualizarFuente(true) },
                label = { Text("Serif") }
            )
            FilterChip(
                selected = !ajustes.usaSerif,
                onClick = { viewModel.actualizarFuente(false) },
                label = { Text("Sans") }
            )
        }
    }
}

@Composable
fun VistaPrevia(
    ajustes: AjustesLectura,
    onTextChange: (String) -> Unit
) {
    val cajaBg = if (ajustes.modoOscuro) Color(0xFF2B2930) else Color(0xFFE6E0E9)
    val colorTextoCaja = if (ajustes.modoOscuro) Color.White else Color.Black
    val fuenteElegida = if (ajustes.usaSerif) FontFamily.Serif else FontFamily.SansSerif

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = "VISTA PREVIA", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = ajustes.textoPersonalizado,
            onValueChange = onTextChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            textStyle = TextStyle(
                fontSize = ajustes.tamano.sp,
                color = colorTextoCaja,
                fontFamily = fuenteElegida,
                lineHeight = (ajustes.tamano * ajustes.interlineado.factor).sp
            )

            )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLecturaScreen() {
    ESTADOSTheme {
        LecturaScreen(modifier = Modifier.fillMaxSize())
    }
}