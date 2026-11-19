package com.example.estados.Screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InscripcionViewModel : ViewModel() {
    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre.asStateFlow()

    private val _correo = MutableStateFlow("")
    val correo: StateFlow<String> = _correo.asStateFlow()

    private val _edad = MutableStateFlow("")
    val edad: StateFlow<String> = _edad.asStateFlow()

    private val _taller = MutableStateFlow("Pintura")
    val taller: StateFlow<String> = _taller.asStateFlow()

    private val _aceptaAviso = MutableStateFlow(false)
    val aceptaAviso: StateFlow<Boolean> = _aceptaAviso.asStateFlow()

    private val _intentoEnviar = MutableStateFlow(false)
    val intentoEnviar: StateFlow<Boolean> = _intentoEnviar.asStateFlow()

    private val _inscrito = MutableStateFlow(false)
    val inscrito: StateFlow<Boolean> = _inscrito.asStateFlow()

    val talleresDisponibles = listOf("Pintura", "Fotografía", "Guitarra")

    fun onNombreChange(nuevoNombre: String) {
        _nombre.value = nuevoNombre
    }

    fun onCorreoChange(nuevoCorreo: String) {
        _correo.value = nuevoCorreo
    }

    fun onEdadChange(nuevaEdad: String) {
        _edad.value = nuevaEdad
    }

    fun onTallerChange(nuevoTaller: String) {
        _taller.value = nuevoTaller
    }

    fun onAvisoChange(acepta: Boolean) {
        _aceptaAviso.value = acepta
    }

    val errorNombre: String?
        get() = if (_nombre.value.length < 3) "El nombre debe tener al menos 3 caracteres" else null

    val errorCorreo: String?
        get() = if (!_correo.value.contains("@") || !_correo.value.contains(".")) "Ingresa un correo valido" else null

    val errorEdad: String?
        get() {
            val valor = _edad.value.toIntOrNull()
            return when {
                valor == null -> "Ingresa solo numeros"
                valor < 12 || valor > 99 -> "La edad debe estar entre 12 y 99"
                else -> null
            }
        }

    fun intentarInscribir() {
        _intentoEnviar.value = true
        if (errorNombre == null && errorCorreo == null && errorEdad == null && _aceptaAviso.value) {
            _inscrito.value = true
        }
    }

    fun reiniciarInscripcion() {
        _nombre.value = ""
        _correo.value = ""
        _edad.value = ""
        _taller.value = "Pintura"
        _aceptaAviso.value = false
        _intentoEnviar.value = false
        _inscrito.value = false
    }
}