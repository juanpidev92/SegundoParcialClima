package com.ejemplo.segundoparcialclima.data.api

import com.ejemplo.segundoparcialclima.data.api.modelos.CiudadDTO
import com.ejemplo.segundoparcialclima.data.api.modelos.ClimaDTO
import com.ejemplo.segundoparcialclima.data.api.modelos.ForecastDTO
import com.ejemplo.segundoparcialclima.data.api.modelos.ListForecastDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class RepositorioApi {

    // Para el parcial está bien hardcodear la API key (igual que el profe)
    private val apiKey = "95e93e4f7a36fc511148468d1774792d"

    private val cliente = HttpClient() {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    suspend fun buscarCiudad(ciudad: String): List<CiudadDTO> {
        val respuesta = cliente.get("https://api.openweathermap.org/geo/1.0/direct") {
            parameter("q", ciudad)
            parameter("limit", 100)
            parameter("appid", apiKey)
        }

        if (respuesta.status == HttpStatusCode.OK) {
            return respuesta.body()
        } else {
            throw Exception("Error al buscar ciudad")
        }
    }

    suspend fun traerClima(lat: Float, lon: Float): ClimaDTO {
        val respuesta = cliente.get("https://api.openweathermap.org/data/2.5/weather") {
            parameter("lat", lat)
            parameter("lon", lon)
            parameter("units", "metric")
            parameter("appid", apiKey)
        }
        if (respuesta.status == HttpStatusCode.OK) {
            return respuesta.body()
        } else {
            throw Exception("Error al traer clima actual")
        }
    }

    suspend fun traerPronostico(nombre: String): List<ListForecastDTO> {
        val respuesta = cliente.get("https://api.openweathermap.org/data/2.5/forecast") {
            parameter("q", nombre)
            parameter("units", "metric")
            parameter("appid", apiKey)
        }
        if (respuesta.status == HttpStatusCode.OK) {
            val forecast = respuesta.body<ForecastDTO>()
            return forecast.list
        } else {
            throw Exception("Error al traer pronóstico")
        }
    }
// Arreglo forecast, para Mi ubicacion
    suspend fun traerPronosticoPorCoords(lat: Float, lon: Float): List<ListForecastDTO> {
        val respuesta = cliente.get("https://api.openweathermap.org/data/2.5/forecast") {
            parameter("lat", lat)
            parameter("lon", lon)
            parameter("units", "metric")
            parameter("appid", apiKey)
        }
        if (respuesta.status == HttpStatusCode.OK) {
            val forecast = respuesta.body<ForecastDTO>()
            return forecast.list
        } else {
            throw Exception("Error al traer pronóstico por coords")
        }
    }
}
