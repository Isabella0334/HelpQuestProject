package com.example.helpquest

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
fun InteractiveMap() {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val geoPoints = remember { mutableStateListOf<Pair<GeoPoint, String>>() } // marcador de coordenadas geopoint

    // conexión hacia firebase / firestore
    val db = FirebaseFirestore.getInstance()

    LaunchedEffect(Unit) {
        db.collection("activity")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    val geoPoint = document.getGeoPoint("coordenadas") // geopoint no array
                    val descripcion = document.getString("descripcion")

                    if (geoPoint != null && descripcion != null) {
                        geoPoints.add(Pair(GeoPoint(geoPoint.latitude, geoPoint.longitude), descripcion))
                        Log.d("Firebase", "GeoPoint: ${geoPoint.latitude}, ${geoPoint.longitude}, Descripción: $descripcion")
                    }
                }
                // centraliza la dirección exacta sin marker
                if (geoPoints.isNotEmpty()) {
                    mapView.controller.setZoom(15)
                    mapView.controller.setCenter(geoPoints[0].first)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Error obteniendo coordenadas: ", exception)
            }
    }

    // marker problemas con geopoint
    LaunchedEffect(geoPoints) {
        geoPoints.forEach { (point, description) ->
            val marker = Marker(mapView).apply {
                position = point
                title = description
            }
            mapView.overlays.add(marker)
        }
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
    Scaffold(
        bottomBar = { CustomBottomNavBar(navController = navController) } // Se utiliza el mismo navbar personalizado
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5DC))
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            // Tarjeta con el mapa interactivo de osmdroid
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(16.dp)
            ) {
                InteractiveMap()
            }

            
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.title),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = stringResource(id = R.string.body_text),
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

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

@Preview(showBackground = true)
@Composable
fun ExploreScreenPreview() {
    ExploreScreen(navController = rememberNavController())
}
