package com.example.estados.Screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class Prioridad {
    ALTA, MEDIA, BAJA
}

enum class Filtro {
    TODAS, PENDIENTES, HECHAS
}

data class Tarea(
    val id: Int,
    val titulo: String,
    val prioridad: Prioridad,
    val hecha: Boolean = false
)

class TareasViewModel : ViewModel() {
    private val _tareas = MutableStateFlow(
        listOf(
            Tarea(1, "Entregar proyecto POO", Prioridad.ALTA, false),
            Tarea(2, "Leer capítulo 4", Prioridad.BAJA, true),
            Tarea(3, "Taller de cálculo", Prioridad.MEDIA, false),
            Tarea(4, "Preparar exposición", Prioridad.MEDIA, false)
        )
    )
    val tareas: StateFlow<List<Tarea>> = _tareas.asStateFlow()

    private val _textoNueva = MutableStateFlow("")
    val textoNueva: StateFlow<String> = _textoNueva.asStateFlow()

    private val _prioridadNueva = MutableStateFlow(Prioridad.ALTA)
    val prioridadNueva: StateFlow<Prioridad> = _prioridadNueva.asStateFlow()

    private val _filtroActual = MutableStateFlow(Filtro.TODAS)
    val filtroActual: StateFlow<Filtro> = _filtroActual.asStateFlow()

    private val _mostrarDialogo = MutableStateFlow(false)
    val mostrarDialogo: StateFlow<Boolean> = _mostrarDialogo.asStateFlow()

    val tareasVisibles: List<Tarea>
        get() = when (_filtroActual.value) {
            Filtro.TODAS -> _tareas.value
            Filtro.PENDIENTES -> _tareas.value.filter { !it.hecha }
            Filtro.HECHAS -> _tareas.value.filter { it.hecha }
        }

    val totalPendientes: Int
        get() = _tareas.value.count { !it.hecha }

    val totalTareas: Int
        get() = _tareas.value.size

    val totalCompletadas: Int
        get() = _tareas.value.count { it.hecha }

    val hayCompletadas: Boolean
        get() = _tareas.value.any { it.hecha }

    fun onTextoNuevaChange(nuevoTexto: String) {
        _textoNueva.value = nuevoTexto
    }

    fun onPrioridadNuevaChange(nuevaPrioridad: Prioridad) {
        _prioridadNueva.value = nuevaPrioridad
    }

    fun onFiltroChange(nuevoFiltro: Filtro) {
        _filtroActual.value = nuevoFiltro
    }

    fun agregarTarea() {
        val tituloLimpio = _textoNueva.value.trim()
        if (tituloLimpio.isNotBlank()) {
            val nuevaId = (_tareas.value.maxOfOrNull { it.id } ?: 0) + 1
            _tareas.value = _tareas.value + Tarea(
                id = nuevaId,
                titulo = tituloLimpio,
                prioridad = _prioridadNueva.value
            )
            _textoNueva.value = ""
        }
    }

    fun cambiarEstadoTarea(tarea: Tarea, hecha: Boolean) {
        _tareas.value = _tareas.value.map { actual ->
            if (actual.id == tarea.id) actual.copy(hecha = hecha) else actual
        }
    }

    fun eliminarTarea(tarea: Tarea) {
        _tareas.value = _tareas.value.filterNot { it.id == tarea.id }
    }

    fun abrirDialogoBorrar() {
        _mostrarDialogo.value = true
    }

    fun cerrarDialogoBorrar() {
        _mostrarDialogo.value = false
    }

    fun borrarCompletadas() {
        _tareas.value = _tareas.value.filterNot { it.hecha }
        _mostrarDialogo.value = false
    }
}