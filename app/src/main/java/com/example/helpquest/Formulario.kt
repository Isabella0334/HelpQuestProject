package com.example.helpquest

import android.os.Bundle
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



// Esta es la función del formulario que se verá dentro de la aplicación

@Composable
fun FormularioDeAplicacion(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8E7)) //Color que deberia de cambiar para que todos tengamos el mismo.
    )
    {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Título centrado y con estilo
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
                        text = stringResource(id= R.string.form_description),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.Gray
                        )
                    )
                }
            }
            // Nombre Completo
            OutlinedTextField(
                value = "", // Cambiar por variable de estado aqui me faltaria implementar variables que tengan un mutablestateof by remember
                onValueChange = { /* Acción para cambiar el valor */ },
                label = { Text(stringResource(id = R.string.full_name_label)) },
                modifier = Modifier.fillMaxWidth()
            )

            // Edad
            OutlinedTextField(
                value = "", // Cambiar por variable de estado aqui me faltaria implementar variables que tengan un mutablestateof by remember
                onValueChange = { /* Acción para cambiar el valor */ },
                label = { Text(stringResource(id = R.string.age_label)) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Documento de identidad
            OutlinedTextField(
                value = "", // Cambiar por variable de estado aqui me faltaria implementar variables que tengan un mutablestateof by remember
                onValueChange = { /* Acción para cambiar el valor */ },
                label = { Text(stringResource(R.string.identity_document_label)) },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Icon(Icons.Default.Add, contentDescription = "Add document")
                }
            )

            // Checkbox de términos y condiciones
            TérminosYCondiciones()

            // Botón de enviar
            EnviarBoton()
        }
    }
}

// Función para el checkbox
@Composable
fun TérminosYCondiciones() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Checkbox(
            checked = false, // Cambiar por variable de estado aqui me faltaria implementar variables que tengan un mutablestateof by remember
            onCheckedChange = { /* Acción para cambiar el estado */ }
        )
        Column {
            Text(stringResource(R.string.terms_checkbox_text))
            Text(
                text = stringResource(R.string.terms_and_conditions),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.Gray,
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier.clickable {
                    // Acción para mostrar términos y condiciones
                }
            )
        }
    }
}

// Función para el botón de enviar
@Composable
fun EnviarBoton() {
    Button(
        onClick = { /* Acción para enviar el formulario */ },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500)) // Cambia backgroundColor por containerColor
    ) {
        Text(text = stringResource(R.string.send_button_text), color = Color.White)
    }
}


