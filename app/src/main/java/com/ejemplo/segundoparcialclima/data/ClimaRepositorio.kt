package com.ejemplo.segundoparcialclima.data

import com.ejemplo.segundoparcialclima.data.api.RepositorioApi
import com.ejemplo.segundoparcialclima.model.Ciudad
import com.ejemplo.segundoparcialclima.model.ClimaDetalle
import com.ejemplo.segundoparcialclima.model.ClimaDia

object ClimaRepositorio {

    private val api = RepositorioApi()

    // Lista local basica
    private val ciudadesLocales = listOf(
        Ciudad(1, "Buenos Aires", -34.6, -58.4),
        Ciudad(2, "Córdoba", -31.4, -64.2),
        Ciudad(3, "Rosario", -32.9, -60.7),
        Ciudad(4, "Mendoza", -32.9, -68.8),
        Ciudad(5, "Mar del Plata", -38.0, -57.5),
    )

    fun obtenerCiudadesLocales(): List<Ciudad> = ciudadesLocales

    suspend fun buscarCiudadesPorNombre(texto: String): List<Ciudad> {
        if (texto.isBlank()) return ciudadesLocales

        val dtos = api.buscarCiudad(texto)

        // Mapear DTO → modelo de dominio
        return dtos.mapIndexed { index, dto ->
            Ciudad(
                id = index, // o algún otro identificador
                nombre = dto.name,
                latitud = dto.lat.toDouble(),
                longitud = dto.lon.toDouble()
            )
        }
    }

    suspend fun obtenerClima(ciudad: Ciudad): ClimaDetalle {
        val climaDTO = api.traerClima(
            lat = ciudad.latitud.toFloat(),
            lon = ciudad.longitud.toFloat()
        )

        val pronosticoDTO = api.traerPronostico(ciudad.nombre)

        val pronostico = pronosticoDTO
            .take(5) // simplificamos: 5 entradas para "5 días"
            .mapIndexed { index, item ->
                ClimaDia(
                    dia = "Día ${index + 1}",
                    tempMin = item.main.temp_min.toInt(),
                    tempMax = item.main.temp_max.toInt(),
                    descripcion = "" // retocar
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

