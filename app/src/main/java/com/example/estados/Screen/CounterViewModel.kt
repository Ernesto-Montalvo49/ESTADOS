package com.example.estados.Screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class LoginEstado {
    INACTIVO, ERROR, BLOQUEADO, EXITO
}

class LoginViewModel : ViewModel() {
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _recordarme = MutableStateFlow(false)
    val recordarme: StateFlow<Boolean> = _recordarme.asStateFlow()

    private val _intentosFallidos = MutableStateFlow(0)
    val intentosFallidos: StateFlow<Int> = _intentosFallidos.asStateFlow()

    private val _estadoLogin = MutableStateFlow(LoginEstado.INACTIVO)
    val estadoLogin: StateFlow<LoginEstado> = _estadoLogin.asStateFlow()

    private val _mostrarPassword = MutableStateFlow(false)
    val mostrarPassword: StateFlow<Boolean> = _mostrarPassword.asStateFlow()

    val bloqueado: Boolean
        get() = _estadoLogin.value == LoginEstado.BLOQUEADO

    val mensajeError: String?
        get() = when (_estadoLogin.value) {
            LoginEstado.ERROR -> "Correo o contraseña incorrectos\n(intento " + _intentosFallidos.value + " de 3)"
            LoginEstado.BLOQUEADO -> "Cuenta Bloqueada"
            else -> null
        }

    val botonHabilitado: Boolean
        get() = !bloqueado && _email.value.isNotBlank() && _password.value.isNotBlank()

    fun onEmailChange(nuevoEmail: String) {
        _email.value = nuevoEmail
    }

    fun onPasswordChange(nuevaPassword: String) {
        _password.value = nuevaPassword
    }

    fun onRecordarmeChange(valor: Boolean) {
        _recordarme.value = valor
    }

    fun alternarMostrarPassword() {
        _mostrarPassword.value = !_mostrarPassword.value
    }

    fun iniciarSesion() {
        if (_email.value == "paciente@vida.mx" && _password.value == "Vida2025") {
            _estadoLogin.value = LoginEstado.EXITO
            _intentosFallidos.value = 0
        } else {
            _intentosFallidos.value++
            if (_intentosFallidos.value >= 3) {
                _estadoLogin.value = LoginEstado.BLOQUEADO
            } else {
                _estadoLogin.value = LoginEstado.ERROR
            }
        }
    }

    fun cerrarSesion() {
        _email.value = ""
        _password.value = ""
        _intentosFallidos.value = 0
        _estadoLogin.value = LoginEstado.INACTIVO
    }
}