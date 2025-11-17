package com.ejemplo.segundoparcialclima.presentacion.clima

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ejemplo.segundoparcialclima.data.ClimaRepositorio
import com.ejemplo.segundoparcialclima.model.Ciudad
import kotlinx.coroutines.launch

class ClimaViewModel : ViewModel() {

    var estado = mutableStateOf(ClimaEstado())
        private set

    fun procesar(intencion: ClimaIntencion) {
        when (intencion) {
            is ClimaIntencion.CargarClima -> cargarClima(intencion.ciudad)
        }
    }

    private fun cargarClima(ciudad: Ciudad) {
        viewModelScope.launch {
            estado.value = estado.value.copy(cargando = true, error = null)
            try {
                val detalle = ClimaRepositorio.obtenerClima(ciudad)
                estado.value = ClimaEstado(
                    cargando = false,
                    clima = detalle,
                    error = null
                )
            } catch (e: Exception) {
                estado.value = ClimaEstado(
                    cargando = false,
                    clima = null,
                    error = "Error al cargar clima"
                )
            }
        }
    }
}

