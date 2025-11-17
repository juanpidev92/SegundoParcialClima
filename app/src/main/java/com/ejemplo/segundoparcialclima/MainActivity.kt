package com.ejemplo.segundoparcialclima

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ejemplo.segundoparcialclima.data.ConfigRepositorio
import com.ejemplo.segundoparcialclima.model.Ciudad
import com.ejemplo.segundoparcialclima.presentacion.ciudades.CiudadesPantalla
import com.ejemplo.segundoparcialclima.presentacion.ciudades.CiudadesViewModel
import com.ejemplo.segundoparcialclima.presentacion.clima.ClimaPantalla
import com.ejemplo.segundoparcialclima.presentacion.clima.ClimaViewModel
import com.ejemplo.segundoparcialclima.presentacion.router.Pantalla
import com.ejemplo.segundoparcialclima.presentacion.router.RouterIntencion
import com.ejemplo.segundoparcialclima.presentacion.router.RouterViewModel
import com.ejemplo.segundoparcialclima.ui.theme.SegundoParcialClimaTheme

class MainActivity : ComponentActivity() {

    // RouterViewModel con factory porque necesita ConfigRepositorio
    private val routerViewModel: RouterViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val configRepositorio = ConfigRepositorio(applicationContext)
                return RouterViewModel(configRepositorio) as T
            }
        }
    }

    private val ciudadesViewModel: CiudadesViewModel by viewModels()
    private val climaViewModel: ClimaViewModel by viewModels()

    // Launcher para pedir permiso de ubicación
    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                obtenerUbicacionYMostrarClima()
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SegundoParcialClimaTheme {
                Surface(color = MaterialTheme.colorScheme.background) {

                    // Al iniciar, pedimos leer la ciudad guardada
                    LaunchedEffect(Unit) {
                        routerViewModel.procesar(RouterIntencion.LeerCiudadGuardada)
                    }

                    val routerEstado = routerViewModel.estado.value

                    when (val pantalla = routerEstado.pantallaActual) {
                        is Pantalla.Ciudades -> {
                            CiudadesPantalla(
                                viewModel = ciudadesViewModel,
                                onCiudadSeleccionada = { ciudad ->
                                    routerViewModel.procesar(
                                        RouterIntencion.IrAClima(ciudad)
                                    )
                                },
                                onBuscarPorUbicacion = {
                                    usarMiUbicacion()
                                }
                            )
                        }

                        is Pantalla.Clima -> {
                            ClimaPantalla(
                                ciudad = pantalla.ciudad,
                                viewModel = climaViewModel,
                                onCambiarCiudad = {
                                    routerViewModel.procesar(RouterIntencion.IrACiudades)
                                },
                                onCompartir = { texto ->
                                    compartirTexto(texto)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Pide permiso si hace falta, o va directo a obtener la ubicación
    private fun usarMiUbicacion() {
        val tienePermiso = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (tienePermiso) {
            obtenerUbicacionYMostrarClima()
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // Lee la última ubicación conocida y navega a la pantalla de clima
    private fun obtenerUbicacionYMostrarClima() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val tienePermiso = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!tienePermiso) return

        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        if (location != null) {
            val lat = location.latitude
            val lon = location.longitude

            val ciudadActual = Ciudad(
                id = -1,
                nombre = "Mi ubicación",
                latitud = lat,
                longitud = lon
            )

            routerViewModel.procesar(
                RouterIntencion.IrAClima(ciudadActual)
            )
        } else {
            Toast.makeText(this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show()
        }
    }

    private fun compartirTexto(texto: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, texto)
        }
        startActivity(Intent.createChooser(intent, "Compartir clima"))
    }
}
