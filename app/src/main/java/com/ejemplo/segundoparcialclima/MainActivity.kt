package com.ejemplo.segundoparcialclima

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ejemplo.segundoparcialclima.data.ConfigRepositorio
import com.ejemplo.segundoparcialclima.model.Ciudad
import com.ejemplo.segundoparcialclima.presentacion.ciudades.CiudadesPantalla
import com.ejemplo.segundoparcialclima.presentacion.ciudades.CiudadesViewModel
import com.ejemplo.segundoparcialclima.presentacion.clima.ClimaPantalla
import com.ejemplo.segundoparcialclima.presentacion.clima.ClimaViewModel
import com.ejemplo.segundoparcialclima.presentacion.router.*
import com.ejemplo.segundoparcialclima.ui.theme.SegundoParcialClimaTheme

class MainActivity : ComponentActivity() {

    // Factory para RouterViewModel porque necesita ConfigRepositorio
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
                                    routerViewModel.procesar(RouterIntencion.IrAClima(ciudad))
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

    private fun compartirTexto(texto: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, texto)
        }
        startActivity(Intent.createChooser(intent, "Compartir clima"))
    }
}
