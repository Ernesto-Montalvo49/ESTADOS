package com.example.estados.Screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

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
    val tareas = mutableStateListOf(
        Tarea(1, "Entregar proyecto POO", Prioridad.ALTA, false),
        Tarea(2, "Leer capítulo 4", Prioridad.BAJA, true),
        Tarea(3, "Taller de cálculo", Prioridad.MEDIA, false),
        Tarea(4, "Preparar exposición", Prioridad.MEDIA, false)
    )

    var textoNueva by mutableStateOf("")
        private set

    var prioridadNueva by mutableStateOf(Prioridad.ALTA)
        private set

    var filtroActual by mutableStateOf(Filtro.TODAS)
        private set

    var mostrarDialogo by mutableStateOf(false)
        private set

    val tareasVisibles: List<Tarea>
        get() = when (filtroActual) {
            Filtro.TODAS -> tareas
            Filtro.PENDIENTES -> tareas.filter { !it.hecha }
            Filtro.HECHAS -> tareas.filter { it.hecha }
        }

    val totalPendientes: Int
        get() = tareas.count { !it.hecha }

    val totalTareas: Int
        get() = tareas.size

    val totalCompletadas: Int
        get() = tareas.count { it.hecha }

    val hayCompletadas: Boolean
        get() = tareas.any { it.hecha }

    fun onTextoNuevaChange(nuevoTexto: String) {
        textoNueva = nuevoTexto
    }

    fun onPrioridadNuevaChange(nuevaPrioridad: Prioridad) {
        prioridadNueva = nuevaPrioridad
    }

    fun onFiltroChange(nuevoFiltro: Filtro) {
        filtroActual = nuevoFiltro
    }

    fun agregarTarea() {
        if (textoNueva.isNotBlank()) {
            val nuevaId = (tareas.maxOfOrNull { it.id } ?: 0) + 1
            tareas.add(Tarea(id = nuevaId, titulo = textoNueva.trim(), prioridad = prioridadNueva))
            textoNueva = ""
        }
    }

    fun cambiarEstadoTarea(tarea: Tarea, hecha: Boolean) {
        val pos = tareas.indexOfFirst { it.id == tarea.id }
        if (pos != -1) {
            tareas[pos] = tarea.copy(hecha = hecha)
        }
    }

    fun eliminarTarea(tarea: Tarea) {
        tareas.removeAll { it.id == tarea.id }
    }

    fun abrirDialogoBorrar() {
        mostrarDialogo = true
    }

    fun cerrarDialogoBorrar() {
        mostrarDialogo = false
    }

    fun borrarCompletadas() {
        tareas.removeAll { it.hecha }
        mostrarDialogo = false
    }
}