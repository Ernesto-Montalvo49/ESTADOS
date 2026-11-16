package com.example.estados.Screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class InscripcionViewModel : ViewModel() {
    var nombre by mutableStateOf("")
        private set

    var correo by mutableStateOf("")
        private set

    var edad by mutableStateOf("")
        private set

    var taller by mutableStateOf("Pintura")
        private set

    var aceptaAviso by mutableStateOf(false)
        private set

    var intentoEnviar by mutableStateOf(false)
        private set

    var inscrito by mutableStateOf(false)
        private set

    val talleresDisponibles = listOf("Pintura", "Fotografía", "Guitarra")

    // Funciones de actualización de campos
    fun onNombreChange(nuevoNombre: String) { nombre = nuevoNombre }
    fun onCorreoChange(nuevoCorreo: String) { correo = nuevoCorreo }
    fun onEdadChange(nuevaEdad: String) { edad = nuevaEdad }
    fun onTallerChange(nuevoTaller: String) { taller = nuevoTaller }
    fun onAvisoChange(acepta: Boolean) { aceptaAviso = acepta }

    val errorNombre: String?
        get() = if (nombre.length < 3) "El nombre debe tener al menos 3 caracteres" else null

    val errorCorreo: String?
        get() = if (!correo.contains("@") || !correo.contains(".")) "Ingresa un correo valido" else null

    val errorEdad: String?
        get() {
            val valor = edad.toIntOrNull()
            return when {
                valor == null -> "Ingresa solo numeros"
                valor < 12 || valor > 99 -> "La edad debe estar entre 12 y 99"
                else -> null
            }
        }

    fun intentarInscribir() {
        intentoEnviar = true
        if (errorNombre == null && errorCorreo == null && errorEdad == null && aceptaAviso) {
            inscrito = true
        }
    }

    fun reiniciarInscripcion() {
        nombre = ""
        correo = ""
        edad = ""
        taller = "Pintura"
        aceptaAviso = false
        intentoEnviar = false
        inscrito = false
    }
}