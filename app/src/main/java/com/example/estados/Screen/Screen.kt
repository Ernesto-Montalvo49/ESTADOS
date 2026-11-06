package com.example.estados.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.estados.ui.theme.ESTADOSTheme

private const val AFORO_MAX = 50

@Composable
fun Screen(modifier: Modifier = Modifier) {
    var actuales by remember { mutableStateOf(32) }
    var visitasDelDia by remember { mutableStateOf(118) }

    val porcentaje = actuales.toFloat() / AFORO_MAX
    val porcentajeTexto = (porcentaje * 100).toInt()

    val (colorTema, textoEstado) = when {
        porcentaje >= 1.0f -> Color(0xFFC62828) to "Aforo completo"
        porcentaje >= 0.7f -> Color(0xFFF57C00) to "Aforo casi lleno"
        else -> Color(0xFF2E7D32) to "Aforo disponible"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Biblioteca Central",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Sala de estudio · capacidad $AFORO_MAX",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth()
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = colorTema.copy(alpha = 0.1f)),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$actuales / $AFORO_MAX",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorTema
                )
                Text(
                    text = "personas en la sala",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(16.dp))

                LinearProgressIndicator(
                    progress = { porcentaje.coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = colorTema,
                    trackColor = colorTema.copy(alpha = 0.2f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "$porcentajeTexto % de ocupación",
                    color = colorTema,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = textoEstado,
                    color = Color.Gray
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { if (actuales > 0) actuales-- },
                enabled = actuales > 0,
                modifier = Modifier
                    .weight(1f)
                    .height(55.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF6750A4))
            ) {
                Text(
                    text = "– Sale",
                    fontSize = 16.sp
                )
            }

            Button(
                onClick = {
                    if (actuales < AFORO_MAX) {
                        actuales++
                        visitasDelDia++
                    }
                },
                enabled = actuales < AFORO_MAX,
                modifier = Modifier
                    .weight(1f)
                    .height(55.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4))
            ) {
                Text(text = "+ Entra", fontSize = 16.sp)
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F2FA)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Visitas del dia",
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Solo suma las entradas",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
                Text(
                    text = "$visitasDelDia",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LeyendaItem(color = Color(0xFF2E7D32), texto = "Verde < 70 %")
            LeyendaItem(color = Color(0xFFF57C00), texto = "Amarillo 70–99 %")
            LeyendaItem(color = Color(0xFFC62828), texto = "Rojo 100 %")
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = {
                actuales = 0
                visitasDelDia = 0
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFB3261E))
        ) {
            Text(text = "Cerrar dia", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
private fun LeyendaItem(color: Color, texto: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color = color, shape = CircleShape)
        )
        Text(text = texto, style = MaterialTheme.typography.bodySmall, fontSize = 11.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    ESTADOSTheme {
        Screen()
    }
}