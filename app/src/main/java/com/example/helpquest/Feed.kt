package com.example.helpquest

import android.util.Log
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.google.firebase.firestore.FirebaseFirestore
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class VolunteerActivity(
    val idA: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val lugar: String = "",
    val tiempo: String = "",
    val skills: String = "",
    val colaborativa: Boolean = false,
    val Imagen: String = "",
    val tipo: List<String> = emptyList(),
    val creator: String = "",
    val coordenadas: GeoPoint = GeoPoint(0.0, 0.0),
    val fechahora: Timestamp = Timestamp.now() // El campo es de tipo Timestamp
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
            // Imagen de la actividad o imagen predeterminada
            Image(
                painter = if (activity.Imagen.isNotEmpty()) { // Usar el campo "Imagen" para la URL
                    rememberAsyncImagePainter(model = activity.Imagen) // Cargar la URL de la imagen
                } else {
                    painterResource(id = R.drawable.img_tarjeta1) // Imagen predeterminada si está vacía
                },
                contentDescription = "Imagen de la actividad",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )

            // Etiquetas (labels) - Opcional si existen etiquetas
            if (activity.tipo.isNotEmpty()) { // Verifica si hay etiquetas disponibles
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    activity.tipo.forEach { label ->
                        LabelChip(label = label, color = Color(0xFF4CAF50)) // Usa un color predeterminado
                    }
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
                    text = activity.tiempo, // Usar el campo "tiempo" de Firestore
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // Título
            Text(
                text = activity.nombre, // Usar el campo "nombre" de Firestore
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            // Descripción
            Text(
                text = activity.descripcion, // Usar el campo "descripcion" de Firestore
                fontSize = 14.sp,
                color = Color.Gray
            )

            // Botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        navController.navigate("info/${activity.idA}") // Navegar pasando el ID de la actividad
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text(text = "Más información")
                }
                Button(
                    onClick = { navController.navigate("Formulario") }, // Navegar a la pantalla "Formulario"
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
                ) {
                    Text(text = "Aplicar")
                }
            }
        }
    }
}


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
    val db = FirebaseFirestore.getInstance()
    var activities by remember { mutableStateOf<List<VolunteerActivity>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        db.collection("activity")
            .get()
            .addOnSuccessListener { result ->
                activities = result.map { document ->
                    document.toObject(VolunteerActivity::class.java).copy(
                        idA = document.id
                    )
                }

                Log.d("Firestore", "Actividades cargadas: $activities")
                isLoading = false
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Error al cargar actividades", exception)
                isLoading = false
            }
    }

    val filteredActivities = activities.filter { activity ->
        activity.nombre.contains(searchQuery, ignoreCase = true) ||
                activity.descripcion.contains(searchQuery, ignoreCase = true) ||
                activity.tipo.any { it.contains(searchQuery, ignoreCase = true) }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { CustomBottomNavBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(Color(0xFFF8EFE8))
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchBar(
                searchQuery = searchQuery,
                onQueryChange = { searchQuery = it }
            )

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (filteredActivities.isEmpty()) {
                Text(
                    text = "No se encontraron actividades.",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            } else {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    filteredActivities.forEach { activity ->
                        CustomCard(activity = activity, navController = navController)
                    }
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

