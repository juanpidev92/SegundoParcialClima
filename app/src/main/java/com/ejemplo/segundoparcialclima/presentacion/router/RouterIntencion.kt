package com.ejemplo.segundoparcialclima.presentacion.router

import com.ejemplo.segundoparcialclima.model.Ciudad

sealed class RouterIntencion {
    object LeerCiudadGuardada : RouterIntencion()
    object IrACiudades : RouterIntencion()
    data class IrAClima(val ciudad: Ciudad) : RouterIntencion()
}
