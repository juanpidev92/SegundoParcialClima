package com.ejemplo.segundoparcialclima.data.api.modelos

import kotlinx.serialization.Serializable

@Serializable
data class ForecastDTO(
    val list: List<ListForecastDTO>
)

@Serializable
data class ListForecastDTO(
    val dt_txt: String,
    val main: MainForecastDTO
)

@Serializable
data class MainForecastDTO(
    val temp_min: Float,
    val temp_max: Float
)
