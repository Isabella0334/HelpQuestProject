package com.example.helpquest

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

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
    val navigation: NavHostController
)

@Composable
fun InfoScreen(navController: NavHostController) {
    val exampleTask = VolunteerTask(
        imageResId = R.drawable.user_image,
        user = "Juan Pérez",
        title = "Limpieza de Playa",
        description = "Ayuda a limpiar las playas de la ciudad. Necesitamos voluntarios comprometidos con el medio ambiente.",
        location = "Playa Central, Ciudad",
        duration = "2 horas - 1 día",
        taskType = "Tarea colaborativa",
        skillsNeeded = "Trabajo en equipo, Conciencia ambiental",
        labels = listOf(
            "Medio Ambiente" to Color.Green,
            "Voluntariado" to Color.Blue,
            "Comunidad" to Color.Magenta
        ),
        navigation = navController
    )

    // Llamar a VolunteerTaskCard con el objeto de ejemplo
    VolunteerTaskCard(task = exampleTask, navController)
}

@Composable
fun VolunteerTaskCard(task: VolunteerTask, navController: NavHostController) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),  // Elevación corregida
        colors = CardDefaults.cardColors(
            containerColor = Color.White  // Color de fondo de la tarjeta
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Sección de imagen y título
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = task.imageResId),
                    contentDescription = "User Image",
                    modifier = Modifier
                        .size(50.dp)
                        .padding(end = 8.dp),
                    contentScale = ContentScale.Crop
                )
                Column {
                    Text(text = task.title, fontSize = 18.sp, color = Color.Black)
                    Text(text = "By: ${task.user}", fontSize = 14.sp, color = Color.Gray)
                }
            }

            // Texto descriptivo con "see more..."
            var expanded by remember { mutableStateOf(false) }
            val shortenedDescription = if (expanded) task.description else task.description.take(100) + "..."

            Text(text = shortenedDescription, fontSize = 14.sp, color = Color.Gray)
            ClickableText(
                text = AnnotatedString(if (expanded) "see less" else "see more..."),
                onClick = { expanded = !expanded },
                style = LocalTextStyle.current.copy(color = Color.Blue, fontSize = 14.sp)
            )

            // Sección de información
            VolunteerTaskInfo(
                location = task.location,
                duration = task.duration,
                taskType = task.taskType,
                skills = task.skillsNeeded,
                navController = navController
            )

            // Etiquetas de voluntariado
            VolunteerTaskLabels(task.labels)

            // Botón de aplicar
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
                painter = painterResource(id = R.drawable.location_icon),  // Asegúrate de tener location_icon en drawable
                contentDescription = "Ubicación",
                modifier = Modifier.size(24.dp)  // Tamaño ajustable
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


