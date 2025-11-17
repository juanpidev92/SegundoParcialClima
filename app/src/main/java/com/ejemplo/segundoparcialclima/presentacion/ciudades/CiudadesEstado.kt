package com.ejemplo.segundoparcialclima.presentacion.ciudades

import com.ejemplo.segundoparcialclima.model.Ciudad

data class CiudadesEstado(
    val cargando: Boolean = false,
    val textoBuscador: String = "",
    val ciudades: List<Ciudad> = emptyList(),
    val error: String? = null
)
