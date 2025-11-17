package com.ejemplo.segundoparcialclima.presentacion.clima

import com.ejemplo.segundoparcialclima.model.Ciudad

sealed class ClimaIntencion {
    data class CargarClima(val ciudad: Ciudad) : ClimaIntencion()
}
