package com.ejemplo.segundoparcialclima.presentacion.ciudades

sealed class CiudadesIntencion {
    object CargarCiudades : CiudadesIntencion()
    data class CambiarTextoBuscador(val texto: String) : CiudadesIntencion()
    object BuscarPorGeo : CiudadesIntencion() // la dejaremos con TODO por ahora
}
