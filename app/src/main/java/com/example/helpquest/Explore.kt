package com.example.helpquest

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore

import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

// de nativo a firebase
@Composable
fun InteractiveMap(geoPoint: GeoPoint) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    // Centraliza el mapa en el geoPoint que se pasa
    LaunchedEffect(geoPoint) {
        mapView.controller.setZoom(15)
        mapView.controller.setCenter(geoPoint)

        val marker = Marker(mapView).apply {
            position = geoPoint
            title = "Ubicación"
        }
        mapView.overlays.add(marker)
    }

    AndroidView(
        factory = { mapView },
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    )
}

@Composable
fun ExploreScreen(navController: NavHostController) {
    val context = LocalContext.current
    val geoPoints = remember { mutableStateListOf<Pair<GeoPoint, String>>() }
    val db = FirebaseFirestore.getInstance()

    // Recuperar datos de Firebase
    LaunchedEffect(Unit) {
        db.collection("activity")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val geoPoint = document.getGeoPoint("coordenadas")
                    val descripcion = document.getString("descripcion")

                    if (geoPoint != null && descripcion != null) {
                        geoPoints.add(Pair(GeoPoint(geoPoint.latitude, geoPoint.longitude), descripcion))
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Error obteniendo coordenadas: ", exception)
            }
    }

    Scaffold(
        bottomBar = { CustomBottomNavBar(navController = navController) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5DC))
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Mostrar tarjetas dinámicamente con datos de Firebase
            geoPoints.forEachIndexed { index, pair ->
                // Tarjeta para cada actividad de carrera
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Actividad ${index + 1}", // Título de la tarjeta
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = pair.second, // Descripción de la actividad
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Mapa dentro de la tarjeta para cada actividad
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp) // Ajuste de tamaño del mapa para la actividad
                        ) {
                            // Aquí pasamos el geoPoint de la actividad actual
                            InteractiveMap(geoPoint = pair.first) // Pasa el geoPoint de cada actividad
                        }

                        // Espaciado adicional entre el mapa y los botones
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = { navController.navigate("Info") },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34C85A))
                            ) {
                                Text(stringResource(id = R.string.more_info), color = Color.White)
                            }

                            Button(
                                onClick = { navController.navigate("Formulario") },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500))
                            ) {
                                Text(stringResource(id = R.string.apply), color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}