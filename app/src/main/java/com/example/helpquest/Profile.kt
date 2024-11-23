package com.example.helpquest

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Date

data class FutureActivity(
    val numActividad: String,
    val fechaActividad: String,
    val ubicacionActividad: String
)

data class PastActivity(
    val numActividad: String,
    val fechaActividad: String
)

data class UserProfile(val nombres: String?, val apellidos: String?)
//haremos una corutina para jalar los datos del nombre y apellidos de firebase
suspend fun getUserProfileFromFirestore(userId: String): UserProfile? {
    return withContext(Dispatchers.IO) {
        try {
            val documentSnapshot = FirebaseFirestore.getInstance()
                .collection("usuarios")
                .document(userId)
                .get()
                .await()

            return@withContext if (documentSnapshot.exists()) {
                val nombres = documentSnapshot.getString("nombres")
                val apellidos = documentSnapshot.getString("apellidos")
                UserProfile(nombres, apellidos)
            } else {
                null
            }
        } catch (e: Exception) {
            println("Error al obtener perfil: ${e.message}")
            null
        }
    }
}

@Composable
fun PantallaPerfil(navController: NavHostController) {
    // Estados para los datos del usuario y actividades
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var futureActivities by remember { mutableStateOf<List<FutureActivity>>(emptyList()) }
    var pastActivities by remember { mutableStateOf<List<PastActivity>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Obtener el uid del usuario autenticado
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid

    // Llamada a Firestore para obtener los datos del usuario y actividades
    LaunchedEffect(userId) {
        if (userId != null) {
            isLoading = true
            try {
                // Obtener datos del usuario
                val userDoc = FirebaseFirestore.getInstance()
                    .collection("usuarios")
                    .document(userId)
                    .get()
                    .await()

                // Nombres del usuario
                firstName = userDoc.getString("nombres") ?: ""
                lastName = userDoc.getString("apellidos") ?: ""

                // Array de actividades
                val activityIds = userDoc.get("activities") as? List<String> ?: emptyList()

                // Obtener actividades y separarlas por fecha
                val activities = activityIds.mapNotNull { activityId ->
                    val activityDoc = FirebaseFirestore.getInstance()
                        .collection("activity")
                        .document(activityId)
                        .get()
                        .await()

                    if (activityDoc.exists()) {
                        val nombre = activityDoc.getString("nombre") ?: "Sin nombre"
                        val fechaHora = activityDoc.getTimestamp("fechahora")?.toDate()
                        val lugar = activityDoc.getString("lugar") ?: "Ubicación desconocida"

                        if (fechaHora != null) {
                            if (fechaHora.after(Date())) {
                                FutureActivity(
                                    numActividad = nombre,
                                    fechaActividad = fechaHora.toString().substring(0, 10), // Fecha
                                    ubicacionActividad = lugar
                                )
                            } else {
                                PastActivity(
                                    numActividad = nombre,
                                    fechaActividad = fechaHora.toString().substring(0, 10) // Fecha
                                )
                            }
                        } else null
                    } else null
                }

                // Separar actividades futuras y pasadas
                futureActivities = activities.filterIsInstance<FutureActivity>()
                pastActivities = activities.filterIsInstance<PastActivity>()
            } catch (e: Exception) {
                println("Error al obtener actividades: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    // Mostrar un indicador de carga mientras obtenemos los datos
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Scaffold(
                bottomBar = { CustomBottomNavBar(navController = navController) }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF8EFE8))
                        .padding(paddingValues)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.pfp),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier.width(60.dp)
                        )
                        Column(modifier = Modifier.padding(40.dp)) {
                            // Mostrar los datos del usuario
                            Text(
                                text = "$firstName $lastName",
                                color = Color(0xFF000000),
                                fontWeight = FontWeight.Bold,
                                fontSize = 25.sp
                            )
                        }
                    }

                    // Tarjetas para actividades futuras y pasadas
                    ProximasActivdadesCard(futureActivities)
                    HistorialCard(pastActivities)
                }
            }
        }
    }
}




@Composable
fun ProximasActivdadesCard(futureActivities: List<FutureActivity>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Próximas Actividades",
                color = Color(0xFF000000),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            futureActivities.forEach { activity ->
                ProxActividadCard(activity)
            }
        }
    }
}


@Composable
fun HistorialCard(pastActivities: List<PastActivity>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Historial de Actividades",
                color = Color(0xFF000000),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            pastActivities.forEach { activity ->
                PastActivityCard(activity)
            }
        }
    }
}


@Composable
fun ProxActividadCard(proxActividad: FutureActivity) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF5DC))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = proxActividad.numActividad, color = Color(0xFF000000))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Icon(
                    imageVector = Icons.Outlined.DateRange,
                    contentDescription = "Date"
                )
                Text(text = proxActividad.fechaActividad, color = Color(0xFF000000))
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "Location"
                )
                Text(text = proxActividad.ubicacionActividad, color = Color(0xFF000000))
            }
        }
    }
}


@Composable
fun PastActivityCard(pastActivity: PastActivity) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF5DC))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = pastActivity.numActividad, color = Color(0xFF000000))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Icon(
                    imageVector = Icons.Outlined.DateRange,
                    contentDescription = "Date"
                )
                Text(text = pastActivity.fechaActividad, color = Color(0xFF000000))
            }
        }
    }
}

