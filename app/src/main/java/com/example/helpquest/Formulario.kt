package com.example.helpquest

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions

@Composable
fun FormularioScreen(modifier: Modifier = Modifier, navController: NavHostController, activityId: String) {
    // Estado para los campos del formulario
    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var documentoIdentidad by remember { mutableStateOf("") }
    var terminosAceptados by remember { mutableStateOf(false) }
    var edadError by remember { mutableStateOf("") }
    var documentoError by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }
    var verificando by remember { mutableStateOf(true) }
    var yaAplico by remember { mutableStateOf(false) }
    var fechaParticipacion by remember { mutableStateOf("") }
    var nombreActividad by remember { mutableStateOf("") }


    // Firebase
    val user = FirebaseAuth.getInstance().currentUser
    val db = FirebaseFirestore.getInstance()

    // Verificar si el usuario ya aplicó
    LaunchedEffect(activityId) {
        if (user != null) {
            val usuarioDoc = db.collection("usuarios").document(user.uid)
            val actividadDoc = db.collection("activity").document(activityId)

            actividadDoc.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val timestamp = document.getTimestamp("fechahora")
                        fechaParticipacion = timestamp?.toDate().toString() ?: "Fecha no disponible"
                        nombreActividad = document.getString("nombre") ?: "Formulario"
                    }
                }
                .addOnFailureListener {
                    Log.e("Formulario", "Error al obtener la fecha de participación", it)
                }


            usuarioDoc.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        nombres = document.getString("nombres") ?: ""
                        apellidos = document.getString("apellidos") ?: ""
                        edad = document.getLong("edad")?.toString() ?: ""
                        documentoIdentidad = document.getLong("documento_identidad")?.toString() ?: ""
                        val activities = document.get("activities") as? List<String> ?: emptyList()
                        yaAplico = activityId in activities
                    }
                    verificando = false
                }
                .addOnFailureListener {
                    Log.e("Formulario", "Error al verificar aplicación previa", it)
                    verificando = false
                }
        } else {
            verificando = false
        }
    }


    // Función para guardar los datos del formulario
    fun enviarDatos() {
        if (user != null) {
            // Validar datos del formulario
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

            // Crear datos del usuario
            val usuarioData = hashMapOf(
                "nombres" to nombres,
                "apellidos" to apellidos,
                "edad" to edadInt,
                "documento_identidad" to documentoInt,
                "terminos_aceptados" to terminosAceptados
            )

            // Guardar datos en Firestore
            val usuarioDoc = db.collection("usuarios").document(user.uid)
            usuarioDoc.set(usuarioData, SetOptions.merge())
                .addOnSuccessListener {
                    // Actualizar la lista de actividades del usuario
                    usuarioDoc.update("activities", FieldValue.arrayUnion(activityId))
                        .addOnSuccessListener {
                            navController.navigate("feed") // Regresar al Feed
                        }
                        .addOnFailureListener { e ->
                            mensajeError = "Error al guardar la actividad: ${e.message}"
                        }
                }
                .addOnFailureListener { e ->
                    mensajeError = "Error al guardar los datos: ${e.message}"
                }
        }
    }

    // UI
    if (verificando) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (yaAplico) {
        // Mostrar mensaje si ya aplicó
        Box(
            modifier = Modifier.fillMaxSize()
                .background(Color(0xFFF8EFE8)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                // Imagen decorativa desde drawable
                Image(
                    painter = painterResource(id = R.drawable.ic_done), // Reemplaza con tu recurso drawable
                    contentDescription = "Advertencia",
                    modifier = Modifier.size(100.dp) // Ajusta el tamaño según tu diseño
                )

                // Texto principal
                Text(
                    text = "¡Espera! Ya aplicaste a esta actividad.",
                    color = Color(0xFFFFA500), // Naranja
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(top = 16.dp)
                )

                // Mostrar la fecha de participación
                if (fechaParticipacion.isNotEmpty()) { // Asegúrate de manejar el estado para la fecha
                    Text(
                        text = "Recuerda que la fecha de participación es el: $fechaParticipacion",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    } else {
        // Mostrar formulario si no ha aplicado
        Card(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8E7))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Campo para Nombres
                OutlinedTextField(
                    value = nombres,
                    onValueChange = { nombres = it },
                    label = { Text("Nombres") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo para Apellidos
                OutlinedTextField(
                    value = apellidos,
                    onValueChange = { apellidos = it },
                    label = { Text("Apellidos") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Edad
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

                // Documento de Identidad
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

                // Checkbox de términos y condiciones
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Checkbox(
                        checked = terminosAceptados,
                        onCheckedChange = { terminosAceptados = it }
                    )
                    Text(
                        text = "Acepto los términos y condiciones",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                // Botón de enviar
                Button(
                    onClick = { enviarDatos() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = nombres.isNotEmpty() &&
                            apellidos.isNotEmpty() &&
                            edad.isNotEmpty() &&
                            documentoIdentidad.isNotEmpty() &&
                            terminosAceptados
                ) {
                    Text(text = "Enviar")
                }

                // Mostrar mensaje de error
                if (mensajeError.isNotEmpty()) {
                    Text(
                        text = mensajeError,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
    }
}