package com.ejemplo.segundoparcialclima.presentacion.router

import com.ejemplo.segundoparcialclima.model.Ciudad

sealed class Pantalla {
    object Ciudades : Pantalla()
    data class Clima(val ciudad: Ciudad) : Pantalla()
}
