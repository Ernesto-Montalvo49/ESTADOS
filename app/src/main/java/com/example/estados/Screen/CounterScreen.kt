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
fun InscripcionScreen(
    modifier: Modifier = Modifier,
    viewModel: InscripcionViewModel = viewModel()
) {
    val nombre by viewModel.nombre.collectAsState()
    val correo by viewModel.correo.collectAsState()
    val edad by viewModel.edad.collectAsState()
    val taller by viewModel.taller.collectAsState()
    val aceptaAviso by viewModel.aceptaAviso.collectAsState()
    val intentoEnviar by viewModel.intentoEnviar.collectAsState()
    val inscrito by viewModel.inscrito.collectAsState()

    if (inscrito) {
        ConfirmacionScreen(
            nombre = nombre,
            correo = correo,
            taller = taller,
            onNueva = { viewModel.reiniciarInscripcion() }
        )
    } else {
        InscripcionForm(
            modifier = modifier,
            viewModel = viewModel,
            nombre = nombre,
            correo = correo,
            edad = edad,
            taller = taller,
            aceptaAviso = aceptaAviso,
            intentoEnviar = intentoEnviar
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InscripcionForm(
    modifier: Modifier = Modifier,
    viewModel: InscripcionViewModel,
    nombre: String,
    correo: String,
    edad: String,
    taller: String,
    aceptaAviso: Boolean,
    intentoEnviar: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Inscripción a talleres", style = MaterialTheme.typography.headlineMedium)
        Text(text = "Casa del Lago · cupo limitado", style = MaterialTheme.typography.bodyMedium)

        CampoTexto(
            label = "Nombre completo",
            value = nombre,
            onValueChange = { viewModel.onNombreChange(it) },
            error = viewModel.errorNombre,
            intentoEnviar = intentoEnviar
        )

        CampoTexto(
            label = "Correo",
            value = correo,
            onValueChange = { viewModel.onCorreoChange(it) },
            error = viewModel.errorCorreo,
            intentoEnviar = intentoEnviar
        )

        CampoTexto(
            label = "Edad",
            value = edad,
            onValueChange = { viewModel.onEdadChange(it) },
            error = viewModel.errorEdad,
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
                viewModel.talleresDisponibles.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            viewModel.onTallerChange(opcion)
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
                onCheckedChange = { viewModel.onAvisoChange(it) }
            )
            Text(text = "Acepto el aviso de privacidad")
        }

        Button(
            onClick = { viewModel.intentarInscribir() },
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
        Text(text = "Nombre: " + nombre, style = MaterialTheme.typography.bodyLarge)
        Text(text = "Correo: " + correo, style = MaterialTheme.typography.bodyLarge)
        Text(text = "Taller: " + taller, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onNueva) {
            Text("Nueva inscripción")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInscripcionScreen() {
    ESTADOSTheme {
        InscripcionScreen()
    }
}