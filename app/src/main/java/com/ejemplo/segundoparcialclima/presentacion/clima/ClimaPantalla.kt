package com.ejemplo.segundoparcialclima.presentacion.clima

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ejemplo.segundoparcialclima.model.Ciudad
import com.ejemplo.segundoparcialclima.model.ClimaDia

@Composable
fun ClimaPantalla(
    ciudad: Ciudad,
    viewModel: ClimaViewModel,
    onCambiarCiudad: () -> Unit,
    onCompartir: (String) -> Unit
) {
    val estado = viewModel.estado.value

    LaunchedEffect(ciudad) {
        viewModel.procesar(ClimaIntencion.CargarClima(ciudad))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = ciudad.nombre,
                style = MaterialTheme.typography.headlineMedium
            )

            TextButton(onClick = onCambiarCiudad) {
                Text("Cambiar ciudad")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (estado.cargando) {
            CircularProgressIndicator()
        } else if (estado.error != null) {
            Text(text = estado.error, color = MaterialTheme.colorScheme.error)
        } else {
            estado.clima?.let { clima ->
                Text(
                    text = "${clima.temperaturaActual} °C - ${clima.descripcionActual}",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(text = "Humedad: ${clima.humedad}%")

                Spacer(modifier = Modifier.height(16.dp))

                // TODO: aquí va el gráfico de máximas y mínimas
                Text(
                    text = "Próximos 5 días",
                    style = MaterialTheme.typography.titleMedium
                )

                LazyColumn {
                    items(clima.pronostico) { dia ->
                        DiaItem(dia)
                        Divider()
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val textoCompartir =
                            "Clima en ${clima.ciudad.nombre}: ${clima.temperaturaActual}°C, ${clima.descripcionActual}"
                        onCompartir(textoCompartir)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Compartir")
                }
            }
        }
    }
}

@Composable
fun DiaItem(dia: ClimaDia) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text = dia.dia, style = MaterialTheme.typography.bodyLarge)
        Text(text = "Min: ${dia.tempMin}°C - Max: ${dia.tempMax}°C")
        Text(text = dia.descripcion)
    }
}
