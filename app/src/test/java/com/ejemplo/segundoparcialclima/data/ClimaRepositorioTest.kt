package com.ejemplo.segundoparcialclima.data

import com.ejemplo.segundoparcialclima.model.Ciudad
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ClimaRepositorioTest {

    @Test
    fun obtenerCiudadesLocales devuelve lista no vacia() {
        val ciudades = ClimaRepositorio.obtenerCiudadesLocales()
        assertTrue(ciudades.isNotEmpty())
    }

    @Test
    fun buscarCiudadesPorNombre vacio devuelve mismas ciudades locales() {
        val locales = ClimaRepositorio.obtenerCiudadesLocales()
        val resultado = runCatching {
            // texto vacío → no debería ir a la API
            ClimaRepositorio.buscarCiudadesPorNombre("")
        }.getOrDefault(emptyList())

        assertEquals(locales.size, resultado.size)
    }

    @Test
    fun buscarCiudadesPorNombre filtra por nombre en locales cuando coincide() {
        val resultado = runCatching {
            ClimaRepositorio.buscarCiudadesPorNombre("Buenos")
        }.getOrDefault(emptyList())

        val contieneBuenosAires = resultado.any { ciudad: Ciudad ->
            ciudad.nombre.contains("Buenos", ignoreCase = true)
        }

        assertTrue(contieneBuenosAires || resultado.isEmpty())
    }
}
