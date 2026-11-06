package com.example.estados.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun InscripcionScreen(modifier: Modifier = Modifier) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var taller by remember { mutableStateOf("Pintura") }
    var aceptaAviso by remember { mutableStateOf(false) }

    var intentoEnviar by remember { mutableStateOf(false) }
    var inscrito by remember { mutableStateOf(false) }

    if (inscrito) {
        ConfirmacionScreen(
            nombre = nombre,
            correo = correo,
            taller = taller,
            onNueva = {
                nombre = ""
                correo = ""
                edad = ""
                taller = "Pintura"
                aceptaAviso = false
                intentoEnviar = false
                inscrito = false
            }
        )
    } else {
        InscripcionForm(
            nombre = nombre,
            onNombreChange = { nombre = it },
            correo = correo,
            onCorreoChange = { correo = it },
            edad = edad,
            onEdadChange = { edad = it },
            taller = taller,
            onTallerChange = { taller = it },
            aceptaAviso = aceptaAviso,
            onAvisoChange = { aceptaAviso = it },
            intentoEnviar = intentoEnviar,
            onInscribirClick = {
                intentoEnviar = true

                val errorNombre = validarNombre(nombre)
                val errorCorreo = validarCorreo(correo)
                val errorEdad = validarEdad(edad)

                if (errorNombre == null && errorCorreo == null && errorEdad == null && aceptaAviso) {
                    inscrito = true
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InscripcionForm(
    nombre: String,
    onNombreChange: (String) -> Unit,
    correo: String,
    onCorreoChange: (String) -> Unit,
    edad: String,
    onEdadChange: (String) -> Unit,
    taller: String,
    onTallerChange: (String) -> Unit,
    aceptaAviso: Boolean,
    onAvisoChange: (Boolean) -> Unit,
    intentoEnviar: Boolean,
    onInscribirClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val talleres = listOf("Pintura", "Fotografía", "Guitarra")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Inscripción a talleres", style = MaterialTheme.typography.headlineMedium)
        Text(text = "Casa del Lago · cupo limitado", style = MaterialTheme.typography.bodyMedium)

        CampoTexto(
            label = "Nombre completo",
            value = nombre,
            onValueChange = onNombreChange,
            error = validarNombre(nombre),
            intentoEnviar = intentoEnviar
        )

        CampoTexto(
            label = "Correo",
            value = correo,
            onValueChange = onCorreoChange,
            error = validarCorreo(correo),
            intentoEnviar = intentoEnviar
        )

        CampoTexto(
            label = "Edad",
            value = edad,
            onValueChange = onEdadChange,
            error = validarEdad(edad),
            intentoEnviar = intentoEnviar
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = taller,
                onValueChange = {},
                readOnly = true,
                label = { Text("Taller") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                talleres.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            onTallerChange(opcion)
                            expanded = false
                        }
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = aceptaAviso,
                onCheckedChange = onAvisoChange
            )
            Text(text = "Acepto el aviso de privacidad")
        }

        Button(
            onClick = onInscribirClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Inscribirme")
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Quedan 6 lugares en este taller",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun CampoTexto(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: String?,
    intentoEnviar: Boolean
) {
    val mostrarError = intentoEnviar && error != null

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = mostrarError,
        supportingText = {
            if (mostrarError) {
                Text(text = error ?: "", color = MaterialTheme.colorScheme.error)
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ConfirmacionScreen(
    nombre: String,
    correo: String,
    taller: String,
    onNueva: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Inscrito", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Nombre: $nombre", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Correo: $correo", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Taller: $taller", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onNueva) {
            Text("Nueva inscripción")
        }
    }
}


fun validarNombre(nombre: String): String? {
    return if (nombre.length < 3) {
        "El nombre debe tener al menos 3 caracteres"
    } else {
        null
    }
}

fun validarCorreo(correo: String): String? {
    return if (!correo.contains("@") || !correo.contains(".")) {
        "Ingresa un correo valido"
    } else {
        null
    }
}

fun validarEdad(edadStr: String): String? {
    val edad = edadStr.toIntOrNull()
    return if (edad == null) {
        "Ingresa solo numeros"
    } else if (edad < 12 || edad > 99) {
        "La edad debe estar entre 12 y 99"
    } else {
        null
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInscripcionScreen() {
    InscripcionScreen()
}