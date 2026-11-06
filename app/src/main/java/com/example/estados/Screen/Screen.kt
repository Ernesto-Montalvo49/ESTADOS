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

enum class Interlineado(val factor: Float) {
    COMPACTO(1.0f),
    NORMAL(1.3f),
    AMPLIO(1.6f)
}

data class AjustesLectura(
    val tamano: Float = 18f,
    val interlineado: Interlineado = Interlineado.NORMAL,
    val modoOscuro: Boolean = false,
    val usaSerif: Boolean = true,
    val textoPersonalizado: String = "Era una noche tranquila en " +
            "el pueblo cuando las campanas comenzaron a sonar, y " +
            "nadie supo por que."
)

@Composable
fun LecturaScreen(modifier: Modifier = Modifier) {
    var ajustes by remember { mutableStateOf(AjustesLectura()) }

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
                    onAjustesChange = { nuevosAjustes ->
                        ajustes = nuevosAjustes
                    },
                    textColor = textColor
                )

                VistaPrevia(
                    ajustes = ajustes,
                    onTextChange = { nuevoTexto ->
                        ajustes = ajustes.copy(textoPersonalizado = nuevoTexto)
                    }
                )
            }

            Button(
                onClick = { ajustes = AjustesLectura() },
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
    onAjustesChange: (AjustesLectura) -> Unit,
    textColor: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Tamaño de letra", color = textColor)
            Text(text = "${ajustes.tamano.toInt()} sp", color = Color(0xFF6750A4), fontWeight = FontWeight.Bold)
        }
        Slider(
            value = ajustes.tamano,
            onValueChange = { nuevoValor ->
                onAjustesChange(ajustes.copy(tamano = nuevoValor))
            },
            valueRange = 14f..32f
        )
        Text(text = "Interlineado", color = textColor)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Interlineado.values().forEach { tipo ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        onAjustesChange(ajustes.copy(interlineado = tipo))
                    }
                ) {
                    RadioButton(
                        selected = ajustes.interlineado == tipo,
                        onClick = { onAjustesChange(ajustes.copy(interlineado = tipo)) }
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
                onCheckedChange = { activo ->
                    onAjustesChange(ajustes.copy(modoOscuro = activo))
                }
            )
        }

        Text(text = "Fuente", color = textColor)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = ajustes.usaSerif,
                onClick = { onAjustesChange(ajustes.copy(usaSerif = true)) },
                label = { Text("Serif") }
            )
            FilterChip(
                selected = !ajustes.usaSerif,
                onClick = { onAjustesChange(ajustes.copy(usaSerif = false)) },
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
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = cajaBg,
                unfocusedContainerColor = cajaBg,
                disabledContainerColor = cajaBg,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLecturaScreen() {
    LecturaScreen(modifier = Modifier.fillMaxSize())
}