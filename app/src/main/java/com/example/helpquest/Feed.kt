package com.example.helpquest

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController


data class VolunteerActivity(
    val imageResId: Int,
    val labels: List<Pair<String, Color>>,
    val time: String,
    val title: String,
    val description: String
)

@Composable
fun CustomCard(activity: VolunteerActivity, navController: NavHostController) {
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
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_clock),
                    contentDescription = "Icono de reloj",
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Tiempo: ",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = activity.time,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
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
                    onClick = { navController.navigate("Info") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text(text = "Más información")
                }
                Button(
                    onClick = { navController.navigate("Formulario") },
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

@Composable
fun CustomBottomNavBar(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF4CAF50))
            .padding(5.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavButton(
            iconResId = R.drawable.ic_location,
            label = "Explore",
            onClick = {
                navController.navigate("explore") {
                    launchSingleTop = true
                    popUpTo("feed") { inclusive = false }
                }
            }
        )
        BottomNavButton(
            iconResId = R.drawable.ic_profile,
            label = "Profile",
            onClick = {
                navController.navigate("Perfil") {
                    launchSingleTop = true
                    popUpTo("feed") { inclusive = false }
                }
            }
        )
        BottomNavButton(
            iconResId = R.drawable.ic_updates,
            label = "Feed",
            onClick = {
                navController.navigate("feed") {
                    launchSingleTop = true
                    popUpTo("feed") { inclusive = false }
                }
            }
        )
    }
}



@Composable
fun BottomNavButton(iconResId: Int, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,

        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = label,
            modifier = Modifier.size(24.dp).padding(bottom = 4.dp)

        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Black
        )
    }
}

@Composable
fun FeedScreen(navController: NavHostController) {

    val allActivities = listOf(
        VolunteerActivity(
            imageResId = R.drawable.img_playa,
            labels = listOf("❤ Comunidad" to Color(0xFF9C27B0), "🌿 Medio Ambiente" to Color(0xFFFFC107)),
            time = "2h - 1 Día",
            title = "Limpieza de Playa",
            description = "Ayuda a limpiar las playas de la ciudad. Necesitamos voluntarios comprometidos con el medio ambiente."
        ),
        VolunteerActivity(
            imageResId = R.drawable.img_uvg,
            labels = listOf("💪 Deporte" to Color(0xFF4CAF50)),
            time = "1h - 3h",
            title = "Carrera UVG",
            description = "¡Ya comenzamos las inscripciones para la Carrera UVG! Esta tiene como objetivo apoyar estudiantes de los tres campus de la Universidad del Valle de Guatemala (UVG)."
        )
    )

    var searchQuery by remember { mutableStateOf("") }

    // Filtrar actividades basadas en la consulta de búsqueda
    val filteredActivities = allActivities.filter { activity ->
        activity.title.contains(searchQuery, ignoreCase = true) ||
                activity.description.contains(searchQuery, ignoreCase = true) ||
                activity.labels.any { it.first.contains(searchQuery, ignoreCase = true) }
    }

    // Mostramos la lista de actividades en el feed
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { CustomBottomNavBar(navController) }

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(Color(0xFFF8EFE8))
                .fillMaxSize()
                .padding(paddingValues) // Añadir paddingValues para manejar la barra de navegación y otras áreas seguras
        ) {
            // Barra de búsqueda
            SearchBar(
                searchQuery = searchQuery,
                onQueryChange = { searchQuery = it }
            )

            // Mostrar actividades filtradas
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                filteredActivities.forEach { activity ->
                    CustomCard(activity = activity, navController = navController)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(searchQuery: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = searchQuery,
        onValueChange = onQueryChange,
        placeholder = { Text("Buscar actividades...") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White, RoundedCornerShape(8.dp)),
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

