package com.ejemplo.segundoparcialclima.data

import com.ejemplo.segundoparcialclima.data.api.RepositorioApi
import com.ejemplo.segundoparcialclima.model.Ciudad
import com.ejemplo.segundoparcialclima.model.ClimaDetalle
import com.ejemplo.segundoparcialclima.model.ClimaDia

object ClimaRepositorio {

    private val api = RepositorioApi()

    // ... ciudadesLocales y obtenerCiudadesLocales() igual que antes ...

    suspend fun obtenerClima(ciudad: Ciudad): ClimaDetalle {
        val climaDTO = api.traerClima(
            lat = ciudad.latitud.toFloat(),
            lon = ciudad.longitud.toFloat()
        )

        // si es "Mi ubicación" (id -1), usamos forecast por coords
        val pronosticoDTO = try {
            if (ciudad.id == -1) {
                api.traerPronosticoPorCoords(
                    lat = ciudad.latitud.toFloat(),
                    lon = ciudad.longitud.toFloat()
                )
            } else {
                api.traerPronostico(ciudad.nombre)
            }
        } catch (e: Exception) {
            // si el forecast falla, devolvemos lista vacía pero NO rompemos toda la pantalla
            emptyList()
        }

        val pronostico = pronosticoDTO
            .take(5)
            .mapIndexed { index, item ->
                ClimaDia(
                    dia = "Día ${index + 1}",
                    tempMin = item.main.temp_min.toInt(),
                    tempMax = item.main.temp_max.toInt(),
                    descripcion = ""
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


