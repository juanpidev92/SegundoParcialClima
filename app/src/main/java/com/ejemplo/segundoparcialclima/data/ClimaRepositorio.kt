package com.ejemplo.segundoparcialclima.data

import com.ejemplo.segundoparcialclima.data.api.RepositorioApi
import com.ejemplo.segundoparcialclima.data.api.modelos.CiudadDTO
import com.ejemplo.segundoparcialclima.data.api.modelos.ListForecastDTO
import com.ejemplo.segundoparcialclima.model.Ciudad
import com.ejemplo.segundoparcialclima.model.ClimaDetalle
import com.ejemplo.segundoparcialclima.model.ClimaDia

object ClimaRepositorio {

    private val api = RepositorioApi()

    // Lista local básica
    private val ciudadesLocales = listOf(
        Ciudad(1, "Buenos Aires", -34.6, -58.4),
        Ciudad(2, "Córdoba", -31.4, -64.2),
        Ciudad(3, "Rosario", -32.9, -60.7),
        Ciudad(4, "Mendoza", -32.9, -68.8),
        Ciudad(5, "Mar del Plata", -38.0, -57.5),
    )

    /** Usado por ConfigRepositorio y CiudadesViewModel */
    fun obtenerCiudadesLocales(): List<Ciudad> = ciudadesLocales

    /** Usado por CiudadesViewModel al filtrar */
    suspend fun buscarCiudadesPorNombre(texto: String): List<Ciudad> {
        // Si está vacío, devolvemos las locales
        if (texto.isBlank()) return ciudadesLocales

        val dtos: List<CiudadDTO> = api.buscarCiudad(texto)

        // Mapear DTO → modelo de dominio
        return dtos.mapIndexed { index, dto ->
            Ciudad(
                id = index,
                nombre = dto.name,
                latitud = dto.lat.toDouble(),
                longitud = dto.lon.toDouble()
            )
        }
    }

    /** Usado por ClimaViewModel */
    suspend fun obtenerClima(ciudad: Ciudad): ClimaDetalle {
        // Clima actual SIEMPRE por lat/lon
        val climaDTO = api.traerClima(
            lat = ciudad.latitud.toFloat(),
            lon = ciudad.longitud.toFloat()
        )

        // Forecast:
        // - Si es "Mi ubicación" (id == -1) → usar lat/lon
        // - Si es una ciudad normal → usar nombre
        val pronosticoDTO: List<ListForecastDTO> = try {
            if (ciudad.id == -1) {
                api.traerPronosticoPorCoords(
                    lat = ciudad.latitud.toFloat(),
                    lon = ciudad.longitud.toFloat()
                )
            } else {
                api.traerPronostico(ciudad.nombre)
            }
        } catch (e: Exception) {
            emptyList()
        }

        val pronostico: List<ClimaDia> = pronosticoDTO
            .take(5)
            .mapIndexed { index, item ->
                ClimaDia(
                    dia = "Día ${index + 1}",
                    tempMin = item.main.temp_min.toInt(),
                    tempMax = item.main.temp_max.toInt(),
                    descripcion = "" // opcional
                )
            }

        return ClimaDetalle(
            ciudad = ciudad,
            temperaturaActual = climaDTO.main.temp.toInt(),
            humedad = climaDTO.main.humidity,
            descripcionActual = climaDTO.weather.firstOrNull()?.description ?: "",
            pronostico = pronostico
        )
    }
}



