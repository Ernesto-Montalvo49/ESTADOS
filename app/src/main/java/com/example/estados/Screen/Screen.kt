package com.example.estados.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class LoginEstado {
    INACTIVO, ERROR, BLOQUEADO, EXITO
}

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var recordarme by remember { mutableStateOf(false) }
    var intentosFallidos by remember { mutableStateOf(0) }
    var estadoLogin by remember { mutableStateOf(LoginEstado.INACTIVO) }
    var mostrarPassword by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        if (estadoLogin == LoginEstado.EXITO) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "¡Bienvenido!", style = MaterialTheme.typography.headlineLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = email, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)

                Spacer(modifier = Modifier.height(32.dp))

                Button(onClick = {
                    email = ""
                    password = ""
                    intentosFallidos = 0
                    estadoLogin = LoginEstado.INACTIVO
                }) {
                    Text(text = "Cerrar sesión")
                }
            }
        } else {
            val mensajeError = when (estadoLogin) {
                LoginEstado.ERROR -> "Incorrecto\n(intento $intentosFallidos de 3)"
                LoginEstado.BLOQUEADO -> "Cuenta Bloqueada"
                else -> null
            }
            val bloqueado = estadoLogin == LoginEstado.BLOQUEADO

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Clínica Vida", style = MaterialTheme.typography.headlineMedium)
                Text(text = "Consulta tus citas médicas", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo") },
                    enabled = !bloqueado,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    enabled = !bloqueado,
                    singleLine = true,
                    visualTransformation = if (mostrarPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        TextButton(onClick = { mostrarPassword = !mostrarPassword }) {
                            Text(text = if (mostrarPassword) "Ocultar" else "Ver")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = recordarme,
                        onCheckedChange = { recordarme = it },
                        enabled = !bloqueado
                    )
                    Text(text = "Recordarme", style = MaterialTheme.typography.bodyMedium)
                }

                if (mensajeError != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = mensajeError,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (email == "paciente@vida.mx" && password == "Vida2025") {
                            estadoLogin = LoginEstado.EXITO
                            intentosFallidos = 0
                        } else {
                            intentosFallidos++
                            if (intentosFallidos >= 3) {
                                estadoLogin = LoginEstado.BLOQUEADO
                            } else {
                                estadoLogin = LoginEstado.ERROR
                            }
                        }
                    },
                    enabled = !bloqueado && email.isNotBlank() && password.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Entrar", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}