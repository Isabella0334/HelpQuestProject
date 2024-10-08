package com.example.helpquest
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class VolunteerTask(
    val imageResId: Int,
    val user: String,
    val title: String,
    val description: String,
    val location: String,
    val duration: String,
    val taskType: String,
    val skillsNeeded: String,
    val labels: List<Pair<String, Color>>
)

@Composable
fun VolunteerTaskCard(task: VolunteerTask) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 4.dp,
        backgroundColor = Color.White
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
                skills = task.skillsNeeded
            )

            // Etiquetas de voluntariado
            VolunteerTaskLabels(task.labels)

            // Botón de aplicar
            Button(
                onClick = { /* Acción de aplicar */ },
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
fun VolunteerTaskInfo(location: String, duration: String, taskType: String, skills: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Place, contentDescription = "Ubicación", tint = Color.Gray)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = location, fontSize = 14.sp, color = Color.Gray)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Schedule, contentDescription = "Duración", tint = Color.Gray)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = duration, fontSize = 14.sp, color = Color.Gray)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Group, contentDescription = "Tipo de tarea", tint = Color.Gray)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = taskType, fontSize = 14.sp, color = Color.Gray)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Star, contentDescription = "Habilidades", tint = Color.Gray)
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

@Composable
fun LabelChip(label: String, color: Color) {
    Surface(
        color = color,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(4.dp)
    ) {
        Text(
            text = label,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 11.sp
        )
    }
}

