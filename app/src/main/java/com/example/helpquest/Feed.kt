package com.example.helpquest

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

// Datos para la tarjeta
data class VolunteerActivity(
    val imageResId: Int,
    val labels: List<Pair<String, Color>>,
    val time: String,
    val title: String,
    val description: String
)

@Composable
fun CustomCard(activity: VolunteerActivity) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = activity.imageResId),
                contentDescription = "Imagen de la actividad",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )
            // Etiquetas
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                activity.labels.forEach { label ->
                    LabelChip(label = label.first, color = label.second)
                }
            }
            // Tiempo
            Text(
                text = activity.time,
                fontSize = 14.sp,
                color = Color.Gray
            )
            // Título
            Text(
                text = activity.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            // Descripción
            Text(
                text = activity.description,
                fontSize = 14.sp,
                color = Color.Gray
            )
            // Botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { /* Acción para Más info */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text(text = "Más información")
                }
                Button(
                    onClick = { /* Acción para Aplicar */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
                ) {
                    Text(text = "Aplicar")
                }
            }
        }
    }
}

// Etiquetas reutilizables
@Composable
fun LabelChip(label: String, color: Color) {
    Surface(
        color = color,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = Color.White,
            fontSize = 11.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomCardList() {
    val activities = listOf(
        VolunteerActivity(
            imageResId = R.drawable.img_playa,
            labels = listOf("❤ Comunidad" to Color(0xFF9C27B0), "🌿 Medio Ambiente" to Color(0xFFFFC107)),
            time = "⏰ 2h - 1 Día",
            title = "Limpieza de Playa",
            description = "Ayuda a limpiar las playas de la ciudad. Necesitamos voluntarios comprometidos con el medio ambiente."
        ),
        VolunteerActivity(
            imageResId = R.drawable.img_uvg,
            labels = listOf("💪 Deporte" to Color(0xFF4CAF50)),
            time = "⏰ 1h - 3h",
            title = "Carrera UVG",
            description = "¡Ya comenzamos las inscripciones para la Carrera UVG!  Esta tiene como objetivo apoyar estudiantes de los tres campus de la Universidad del Valle de Guatemala (UVG)."
        )
    )

    Column(modifier = Modifier.background(Color(0xFFF8EFE8))) {
        activities.forEach { activity ->
            CustomCard(activity = activity)
        }
    }
}
