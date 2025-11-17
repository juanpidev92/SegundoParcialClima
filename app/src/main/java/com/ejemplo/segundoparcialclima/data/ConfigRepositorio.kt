package com.ejemplo.segundoparcialclima.data

import android.content.Context
import com.ejemplo.segundoparcialclima.model.Ciudad

class ConfigRepositorio(private val context: Context) {

    private val prefs = context.getSharedPreferences("config_clima", Context.MODE_PRIVATE)

    fun guardarCiudad(ciudad: Ciudad) {
        prefs.edit()
            .putInt("ciudad_id", ciudad.id)
            .apply()
    }

    fun leerCiudadGuardada(): Ciudad? {
        val id = prefs.getInt("ciudad_id", -1)
        if (id == -1) return null

        // ⬇⬇ CAMBIO IMPORTANTE: usamos obtenerCiudadesLocales()
        return ClimaRepositorio
            .obtenerCiudadesLocales()
            .find { ciudad -> ciudad.id == id }
    }

    fun limpiarCiudad() {
        prefs.edit().remove("ciudad_id").apply()
    }
}

