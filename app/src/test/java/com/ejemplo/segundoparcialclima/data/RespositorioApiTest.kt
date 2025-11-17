package com.ejemplo.segundoparcialclima.data

import com.ejemplo.segundoparcialclima.data.api.RepositorioApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RespositorioApiTest {

    private val api = RepositorioApi()

    @Test
    fun `buscarCiudad devuelve lista no vacia para ciudad valida`() = runBlocking {
        val resultado = api.buscarCiudad("Buenos Aires")

        // Si hay conexión y la API responde bien, debería devolver al menos 1 ciudad
        assertTrue(resultado.isNotEmpty())
    }

    @Test
    fun `traerClima devuelve objeto de clima para coordenadas validas`() = runBlocking {
        // Coordenadas aproximadas de Buenos Aires
        val lat = -34.6f
        val lon = -58.4f

        val clima = api.traerClima(lat, lon)

        // Verificamos que venga algo y que la temperatura actual tenga sentido
        assertNotNull(clima)
        assertNotNull(clima.main)
    }

    @Test
    fun `traerPronostico devuelve lista no vacia para ciudad valida`() = runBlocking {
        val pronostico = api.traerPronostico("Buenos Aires")

        assertTrue(pronostico.isNotEmpty())
    }
}

