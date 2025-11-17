package com.ejemplo.segundoparcialclima.data.api.modelos

import kotlinx.serialization.Serializable

@Serializable
data class ClimaDTO(
    val name: String,
    val main: MainDTO,
    val weather: List<WeatherDTO>
)

@Serializable
data class MainDTO(
    val temp: Float,
    val humidity: Int
)

@Serializable
data class WeatherDTO(
    val description: String
)
