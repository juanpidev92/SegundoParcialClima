package com.ejemplo.segundoparcialclima.model

data class ClimaDia(
    val dia: String,
    val tempMin: Int,
    val tempMax: Int,
    val descripcion: String
)

data class ClimaDetalle(
    val ciudad: Ciudad,
    val temperaturaActual: Int,
    val humedad: Int,
    val descripcionActual: String,
    val pronostico: List<ClimaDia>
)
