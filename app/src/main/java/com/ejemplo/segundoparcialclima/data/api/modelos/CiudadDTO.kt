package com.ejemplo.segundoparcialclima.data.api.modelos

import kotlinx.serialization.Serializable

@Serializable
data class CiudadDTO(
    val name: String,
    val lat: Float,
    val lon: Float,
    val country: String? = null
)
