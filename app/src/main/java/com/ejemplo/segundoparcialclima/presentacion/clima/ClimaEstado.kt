package com.ejemplo.segundoparcialclima.presentacion.clima

import com.ejemplo.segundoparcialclima.model.ClimaDetalle

data class ClimaEstado(
    val cargando: Boolean = false,
    val clima: ClimaDetalle? = null,
    val error: String? = null
)
