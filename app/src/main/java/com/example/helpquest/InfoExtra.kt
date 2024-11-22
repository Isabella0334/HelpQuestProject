package com.example.helpquest

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale


data class VolunteerTask(
    val imageResId: Int,
    val user: String,
    val title: String,
    val description: String,
    val location: String,
    val duration: String,
    val taskType: String,
    val skillsNeeded: String,
    val labels: List<Pair<String, Color>>,
    val navigation: NavHostController,
    val imageUrl: String = "",
)

@Composable
fun InfoScreen(navController: NavHostController, idA: String?) {
    val db = FirebaseFirestore.getInstance()
    var activity by remember { mutableStateOf<VolunteerActivity?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Recuperar los datos desde Firestore
    LaunchedEffect(idA) {
        if (idA != null) {
            db.collection("activity").document(idA)
                .get()
                .addOnSuccessListener { document ->
                    activity = document.toObject(VolunteerActivity::class.java)
                    isLoading = false
                }
                .addOnFailureListener { exception ->
                    isLoading = false
                    Log.e("FirestoreError", "Error al cargar la actividad", exception)
                }
        }
    }

    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else if (activity != null) {
        VolunteerTaskCard(
            task = VolunteerTask(
                imageResId = 0, // Cambiar al recurso predeterminado si no tienes imágenes locales
                user = activity!!.creator,
                title = activity!!.nombre,
                description = activity!!.descripcion,
                location = activity!!.lugar,
                duration = activity!!.tiempo,
                taskType = if (activity!!.colaborativa) "Colaborativa" else "Solo",
                skillsNeeded = if (activity!!.skills.isNotEmpty()) activity!!.skills else "No especificadas",
                labels = activity!!.tipo.map { it to Color(0xFF4CAF50) }, // Usa color predeterminado
                navigation = navController
            ),
            navController = navController
        )
    } else {
        Text(
            text = "No se encontró la actividad.",
            modifier = Modifier.fillMaxSize(),
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun VolunteerTaskCard(task: VolunteerTask, navController: NavHostController) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Imagen de la actividad
            Image(
                painter = if (task.imageUrl.isNotEmpty()) { // Usar la URL de la imagen si está disponible
                    rememberAsyncImagePainter(model = task.imageUrl) // Cargar la imagen desde la URL
                } else {
                    painterResource(id = R.drawable.img_tarjeta1) // Imagen predeterminada si no hay URL
                },
                contentDescription = "Imagen de la actividad",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            // Título y usuario
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = task.title, fontSize = 18.sp, color = Color.Black)
                Text(text = "Creado por: ${task.user}", fontSize = 14.sp, color = Color.Gray)
            }

            // Descripción
            Text(text = task.description, fontSize = 14.sp, color = Color.Gray)

            // Información adicional
            VolunteerTaskInfo(
                location = task.location,
                duration = task.duration,
                taskType = task.taskType,
                skills = task.skillsNeeded,
                navController = navController
            )

            // Etiquetas
            VolunteerTaskLabels(task.labels)

            // Botón para aplicar
            Button(
                onClick = { navController.navigate("Formulario") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
            ) {
                Text(text = "Aplicar")
            }
        }
    }
}


@Composable
fun VolunteerTaskInfo(location: String, duration: String, taskType: String, skills: String, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.location_icon),
                contentDescription = "Ubicación",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))  // Espaciado entre el ícono y el texto
            Text(text = location, fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.weight(1f))  // Empuja la flecha a la derecha

            // Flecha presionable
            IconButton(onClick = { navController.navigate("Explore") }) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,  // Flecha hacia la derecha
                    contentDescription = "Ir a ubicación",
                    modifier = Modifier.size(24.dp),  // Tamaño ajustable
                    tint = Color.Gray
                )
            }
        }

        // Duración (Imagen local en lugar de Icon)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.duration_icon),  // Asegúrate de tener duration_icon en drawable
                contentDescription = "Duración",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = duration, fontSize = 14.sp, color = Color.Gray)
        }

        // Tipo de tarea (Imagen local en lugar de Icon)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.task_type_icon),  // Asegúrate de tener task_type_icon en drawable
                contentDescription = "Tipo de tarea",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = taskType, fontSize = 14.sp, color = Color.Gray)
        }

        // Habilidades (Imagen local en lugar de Icon)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.skills_icon),  // Asegúrate de tener skills_icon en drawable
                contentDescription = "Habilidades",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = skills, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
fun VolunteerTaskLabels(labels: List<Pair<String, Color>>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        labels.forEach { label ->
            LabelChip(label.first, label.second)
        }
    }
}


