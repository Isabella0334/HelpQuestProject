package com.example.helpquest

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


// Esta es la función del formulario que se verá dentro de la aplicación
@Composable
fun FormularioScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    // Estado para los campos del formulario
    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var documentoIdentidad by remember { mutableStateOf("") }
    var terminosAceptados by remember { mutableStateOf(false) }
    var edadError by remember { mutableStateOf("") }
    var documentoError by remember { mutableStateOf("") }

    // Obtener el usuario actuall
    val user = FirebaseAuth.getInstance().currentUser
    val db = FirebaseFirestore.getInstance()

    LaunchedEffect(user?.uid) {
        if (user != null) {
            // Si el usuario está autenticado, obtenemos los datos de Firestore
            val docRef = db.collection("usuarios").document(user.uid)
            docRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    // Si los datos existen, los cargamos en los campos
                    nombres = document.getString("nombres") ?: ""
                    apellidos = document.getString("apellidos") ?: ""
                    edad = document.getLong("edad")?.toString() ?: ""
                    documentoIdentidad = document.getLong("documento_identidad")?.toString() ?: ""
                    terminosAceptados = document.getBoolean("terminos_aceptados") ?: false
                }
            }.addOnFailureListener {
                // Si hubo un error al cargar los datos, los campos permanecerán vacíos
                Log.e("Formulario", "Error al cargar los datos del usuario")
            }
        }
    }

    // Esta función enviará los datos a Firestore
    fun enviarDatos() {
        if (user != null) {
            // Validar que la edad sea un número válido
            val edadInt = edad.toIntOrNull()
            if (edadInt == null || edadInt <= 0 || edadInt >= 200) {
                edadError = "Por favor ingresa una edad válida"
                return // Si la edad no es válida, no enviamos el formulario
            }

            // Validar que el documento de identidad sea un número válido
            val documentoInt = documentoIdentidad.toIntOrNull()
            if (documentoInt == null || documentoInt <= 0) {
                documentoError = "Por favor ingresa un documento de identidad válido"
                return // Si el documento no es válido, no enviamos el formulario
            }

            // Crear un mapa con los datos del formulario
            val usuarioData = hashMapOf(
                "nombres" to if (nombres.isNotBlank()) nombres else null,
                "apellidos" to if (apellidos.isNotBlank()) apellidos else null,
                "edad" to edadInt,
                "documento_identidad" to documentoInt,
                "terminos_aceptados" to terminosAceptados
            )

            // Actualizar el documento del usuario con el UID
            db.collection("usuarios")
                .document(user.uid) // Documento identificado por el UID de Firebase Authentication
                .set(usuarioData, SetOptions.merge()) // Usamos merge para no sobrescribir otros datos
                .addOnSuccessListener {
                    // Si el formulario se envía correctamente pues regresamos al feed
                    navController.navigate("feed")
                }
                .addOnFailureListener { exception ->
                    println("Error al enviar los datos: ${exception.message}")
                }
        }
    }

    // Formulario UI
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
            // Título
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = stringResource(id = R.string.form_title),
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = stringResource(id = R.string.form_description),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.Gray
                        )
                    )
                }
            }

            // Campo para Nombres
            OutlinedTextField(
                value = nombres,
                onValueChange = { nombres = it },
                label = { Text(stringResource(id = R.string.first_name_label)) },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo para Apellidos
            OutlinedTextField(
                value = apellidos,
                onValueChange = { apellidos = it },
                label = { Text(stringResource(id = R.string.last_name_label)) },
                modifier = Modifier.fillMaxWidth()
            )

            // Edad
            OutlinedTextField(
                value = edad,
                onValueChange = { edad = it; edadError = "" }, // Limpiar error al cambiar valor
                label = { Text(stringResource(id = R.string.age_label)) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                isError = edadError.isNotEmpty()
            )

            // Mostrar error si la edad no es válida
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
                onValueChange = { documentoIdentidad = it; documentoError = "" }, // Limpiar error al cambiar valor
                label = { Text(stringResource(R.string.identity_document_label)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                isError = documentoError.isNotEmpty()
            )

            // Mostrar error si el documento de identidad no es válido
            if (documentoError.isNotEmpty()) {
                Text(
                    text = documentoError,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            // Checkbox de términos y condiciones
            TérminosYCondiciones(terminosAceptados) { terminosAceptados = it }

            // Botón de enviar, habilitado solo si los términos son aceptados
            EnviarBoton(
                onClick = { enviarDatos() },
                enabled = nombres.isNotEmpty() &&
                        apellidos.isNotEmpty() &&
                        edad.isNotEmpty() &&
                        documentoIdentidad.isNotEmpty() &&
                        terminosAceptados
            )
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
