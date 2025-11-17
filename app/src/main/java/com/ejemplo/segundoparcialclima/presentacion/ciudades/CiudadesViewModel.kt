package com.ejemplo.segundoparcialclima.presentacion.ciudades

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ejemplo.segundoparcialclima.data.ClimaRepositorio
import kotlinx.coroutines.launch

class CiudadesViewModel : ViewModel() {

    var estado = mutableStateOf(CiudadesEstado())
        private set

    fun procesar(intencion: CiudadesIntencion) {
        when (intencion) {
            CiudadesIntencion.CargarCiudades -> cargarCiudades()
            is CiudadesIntencion.CambiarTextoBuscador -> {
                estado.value = estado.value.copy(textoBuscador = intencion.texto)
                filtrarCiudades()
            }
            CiudadesIntencion.BuscarPorGeo -> {
                // TODO: implementar geolocalización
            }
        }
    }

    private fun cargarCiudades() {
        // Por ahora, lista local para arrancar rápido
        viewModelScope.launch {
            estado.value = estado.value.copy(cargando = true, error = null)
            try {
                val lista = ClimaRepositorio.obtenerCiudadesLocales()
                estado.value = estado.value.copy(
                    cargando = false,
                    ciudades = lista
                )
            } catch (e: Exception) {
                estado.value = estado.value.copy(
                    cargando = false,
                    error = "Error al cargar ciudades"
                )
            }
        }
    }

    private fun filtrarCiudades() {
        viewModelScope.launch {
            try {
                val texto = estado.value.textoBuscador
                val lista = ClimaRepositorio.buscarCiudadesPorNombre(texto)
                estado.value = estado.value.copy(
                    ciudades = lista,
                    error = null
                )
            } catch (e: Exception) {
                estado.value = estado.value.copy(
                    error = "Error al buscar ciudades"
                )
            }
        }
    }
}

