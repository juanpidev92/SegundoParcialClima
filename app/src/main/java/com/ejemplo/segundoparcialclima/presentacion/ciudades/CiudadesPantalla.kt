package com.ejemplo.segundoparcialclima.presentacion.ciudades

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ejemplo.segundoparcialclima.model.Ciudad

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CiudadesPantalla(
    viewModel: CiudadesViewModel,
    onCiudadSeleccionada: (Ciudad) -> Unit,
    onBuscarPorUbicacion: () -> Unit   // ⬅ nuevo parámetro
) {
    val estado = viewModel.estado.value

    LaunchedEffect(Unit) {
        viewModel.procesar(CiudadesIntencion.CargarCiudades)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = estado.textoBuscador,
            onValueChange = {
                viewModel.procesar(CiudadesIntencion.CambiarTextoBuscador(it))
            },
            label = { Text("Buscar ciudad") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ⬇ Botón para usar la ubicación actual
        Button(
            onClick = { onBuscarPorUbicacion() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Usar mi ubicación")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (estado.cargando) {
            CircularProgressIndicator()
        } else if (estado.error != null) {
            Text(text = estado.error, color = MaterialTheme.colorScheme.error)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(estado.ciudades) { ciudad ->
                    CiudadItem(
                        ciudad = ciudad,
                        onClick = { onCiudadSeleccionada(ciudad) }
                    )
                    Divider()
                }
            }
        }
    }
}

@Composable
fun CiudadItem(
    ciudad: Ciudad,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp)
    ) {
        Text(text = ciudad.nombre, style = MaterialTheme.typography.bodyLarge)
    }
}
