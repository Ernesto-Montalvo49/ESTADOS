package com.example.estados.Screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

enum class LoginEstado {
    INACTIVO, ERROR, BLOQUEADO, EXITO
}

class LoginViewModel : ViewModel() {
    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var recordarme by mutableStateOf(false)
        private set

    var intentosFallidos by mutableStateOf(0)
        private set

    var estadoLogin by mutableStateOf(LoginEstado.INACTIVO)
        private set

    var mostrarPassword by mutableStateOf(false)
        private set

    val bloqueado: Boolean
        get() = estadoLogin == LoginEstado.BLOQUEADO

    val mensajeError: String?
        get() = when (estadoLogin) {
            LoginEstado.ERROR -> "Correo o contraseña incorrectos\n(intento " + intentosFallidos + " de 3)"
            LoginEstado.BLOQUEADO -> "Cuenta Bloqueada"
            else -> null
        }

    val botonHabilitado: Boolean
        get() = !bloqueado && email.isNotBlank() && password.isNotBlank()

    fun onEmailChange(nuevoEmail: String) {
        email = nuevoEmail
    }

    fun onPasswordChange(nuevaPassword: String) {
        password = nuevaPassword
    }

    fun onRecordarmeChange(valor: Boolean) {
        recordarme = valor
    }

    fun alternarMostrarPassword() {
        mostrarPassword = !mostrarPassword
    }

    fun iniciarSesion() {
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
    }

    fun cerrarSesion() {
        email = ""
        password = ""
        intentosFallidos = 0
        estadoLogin = LoginEstado.INACTIVO
    }
}