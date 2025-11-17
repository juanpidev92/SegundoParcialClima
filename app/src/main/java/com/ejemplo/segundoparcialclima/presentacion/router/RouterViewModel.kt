package com.ejemplo.segundoparcialclima.presentacion.router

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ejemplo.segundoparcialclima.data.ConfigRepositorio
import kotlinx.coroutines.launch

class RouterViewModel(
    private val configRepositorio: ConfigRepositorio
) : ViewModel() {

    // Estado inicial: por defecto Ciudades (se corrige al leer ciudad guardada)
    private val _estado = androidx.compose.runtime.mutableStateOf(
        RouterEstado(pantallaActual = Pantalla.Ciudades)
    )
    val estado: androidx.compose.runtime.State<RouterEstado> = _estado

    fun procesar(intencion: RouterIntencion) {
        when (intencion) {
            RouterIntencion.LeerCiudadGuardada -> leerCiudadGuardada()
            RouterIntencion.IrACiudades -> {
                _estado.value = RouterEstado(Pantalla.Ciudades)
            }
            is RouterIntencion.IrAClima -> {
                configRepositorio.guardarCiudad(intencion.ciudad)
                _estado.value = RouterEstado(Pantalla.Clima(intencion.ciudad))
            }
        }
    }

    private fun leerCiudadGuardada() {
        viewModelScope.launch {
            val ciudad = configRepositorio.leerCiudadGuardada()
            _estado.value = if (ciudad != null) {
                RouterEstado(Pantalla.Clima(ciudad))
            } else {
                RouterEstado(Pantalla.Ciudades)
            }
        }
    }
}

