package com.example.helpquest


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.uvg.example.lab06api.FirebaseRepository
import kotlinx.coroutines.launch

@Composable
fun FormularioScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    repository: FirebaseRepository = FirebaseRepository()
) {
    val coroutineScope = androidx.compose.runtime.rememberCoroutineScope()

    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var documentoIdentidad by remember { mutableStateOf("") }
    var terminosAceptados by remember { mutableStateOf(false) }
    var edadError by remember { mutableStateOf("") }
    var documentoError by remember { mutableStateOf("") }

    // Obtener los datos iniciales del usuario
    LaunchedEffect(Unit) {
        val datosUsuario = repository.obtenerDatosUsuario()
        datosUsuario?.let { datos ->
            nombres = datos["nombres"] as? String ?: ""
            apellidos = datos["apellidos"] as? String ?: ""
            edad = (datos["edad"] as? Long)?.toString() ?: ""
            documentoIdentidad = (datos["documento_identidad"] as? Long)?.toString() ?: ""
            terminosAceptados = datos["terminos_aceptados"] as? Boolean ?: false
        }
    }

    // Función para enviar datos
    suspend fun enviarDatos() {
        val edadInt = edad.toIntOrNull()
        if (edadInt == null || edadInt <= 0 || edadInt >= 200) {
            edadError = "Por favor ingresa una edad válida"
            return
        }

        val documentoInt = documentoIdentidad.toIntOrNull()
        if (documentoInt == null || documentoInt <= 0) {
            documentoError = "Por favor ingresa un documento de identidad válido"
            return
        }

        val usuarioData = mapOf(
            "nombres" to nombres.ifBlank { "" },
            "apellidos" to apellidos.ifBlank { "" },
            "edad" to edadInt,
            "documento_identidad" to documentoInt,
            "terminos_aceptados" to terminosAceptados
        )

        try {
            repository.enviarDatosUsuario(usuarioData)
            navController.navigate("feed")
        } catch (e: Exception) {
            println("Error al enviar los datos: ${e.message}")
        }
    }

    // UI del formulario
    Card(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8E7))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = nombres,
                onValueChange = { nombres = it },
                label = { Text("Nombres") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = apellidos,
                onValueChange = { apellidos = it },
                label = { Text("Apellidos") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = edad,
                onValueChange = { edad = it; edadError = "" },
                label = { Text("Edad") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                isError = edadError.isNotEmpty()
            )

            if (edadError.isNotEmpty()) {
                Text(
                    text = edadError,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            OutlinedTextField(
                value = documentoIdentidad,
                onValueChange = { documentoIdentidad = it; documentoError = "" },
                label = { Text("Documento de Identidad") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                isError = documentoError.isNotEmpty()
            )

            if (documentoError.isNotEmpty()) {
                Text(
                    text = documentoError,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = terminosAceptados,
                    onCheckedChange = { terminosAceptados = it }
                )
                Text(text = "Acepto los términos y condiciones")
            }

            Button(
                onClick = {
                    coroutineScope.launch {
                        enviarDatos() // Llama la función suspend dentro de la corutina
                    }
                },
                enabled = nombres.isNotEmpty() && apellidos.isNotEmpty() &&
                        edad.isNotEmpty() && documentoIdentidad.isNotEmpty() && terminosAceptados,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar")
            }
        }
    }
}



// Función para el checkbox de términos y condiciones
@Composable
fun TérminosYCondiciones(terminosAceptados: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Checkbox(
            checked = terminosAceptados,
            onCheckedChange = onCheckedChange
        )
        Text(
            text = stringResource(R.string.terms_checkbox_text),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

// Función para el botón de enviar
@Composable
fun EnviarBoton(onClick: () -> Unit, enabled: Boolean) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500)),
        enabled = enabled
    ) {
        Text(text = stringResource(R.string.send_button_text), color = Color.White)
    }
}
