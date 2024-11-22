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
    // Estados para los datos del usuario
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) } // Indicador de carga

    // Obtener el uid del usuario autenticado
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid

    // Llamada a Firestore para obtener los datos del usuario
    LaunchedEffect(userId) {
        if (userId != null) {
            // Obtener datos del usuario desde Firestore
            val userProfile = getUserProfileFromFirestore(userId)
            if (userProfile != null) {
                firstName = userProfile.nombres ?: ""
                lastName = userProfile.apellidos ?: ""
            }
            isLoading = false // Termina la carga
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
                bottomBar = { CustomBottomNavBar(navController = navController) } // Añadido el CustomBottomNavBar
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
                                text = "$firstName $lastName", // Mostramos nombre completo
                                fontWeight = FontWeight.Bold,
                                fontSize = 25.sp
                            )
                        }
                    }

                    // Agregar las tarjetas de actividades, logros, historial, etc.
                    ProximasActivdadesCard()
                    LogrosCard()
                    HistorialCard()
                }
            }
        }
    }
}



@Composable
fun ProximasActivdadesCard(modifier: Modifier = Modifier) {
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
            Box(modifier = Modifier.fillMaxWidth()) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = stringResource(id = R.string.prox_actividades),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }

            ProximaActividadList()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF28B3E)),
                    onClick = {/* TODO */}
                ) {
                    Text(stringResource(id = R.string.mas_info))
                }

            }
        }
    }
}

@Composable
fun LogrosCard(modifier: Modifier = Modifier) {
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
            Box(modifier = Modifier.fillMaxWidth()) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = stringResource(id = R.string.logros),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.medalla),
                    contentDescription = "medalla",
                    modifier = Modifier.width(50.dp)
                )
                Image(
                    painter = painterResource(R.drawable.trofeo),
                    contentDescription = "trofeo",
                    modifier = Modifier.width(50.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF28B3E)),
                    onClick = {/* TODO */}
                ) {
                    Text(
                        text = stringResource(id = R.string.mas_info),
                        textAlign = TextAlign.Center
                    )
                }

            }
        }
    }
}

@Composable
fun HistorialCard(modifier: Modifier = Modifier) {
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
            Box(modifier = Modifier.fillMaxWidth()) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = stringResource(id = R.string.historial_actividades),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }

            PastActivityList()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF28B3E)), // 0xFFF28B3E
                    onClick = {/* TODO */}
                ) {
                    Text(stringResource(id = R.string.mas_info))
                }

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
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD9D9D9))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = proxActividad.numActividad)
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
                Text(text = proxActividad.fechaActividad)
                Spacer(modifier = Modifier.height(20.dp))
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "Location"
                )
                Text(text = proxActividad.ubicacionActividad)
            }
        }
    }
}

@Composable
fun ProximaActividadList() {
    val proximasActividades = listOf(
        FutureActivity(
            numActividad = stringResource(id = R.string.prox_actividad1),
            fechaActividad = stringResource(id = R.string.fecha_actividad),
            ubicacionActividad = stringResource(id = R.string.ubicacion_actividad)
        ),
        FutureActivity(
            numActividad = stringResource(id = R.string.prox_actividad2),
            fechaActividad = stringResource(id = R.string.fecha_actividad),
            ubicacionActividad = stringResource(id = R.string.ubicacion_actividad)
        )
    )

    Column(modifier = Modifier) {
        proximasActividades.forEach { proxActividad ->
            ProxActividadCard(proxActividad = proxActividad)
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
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD6EBC5))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = pastActivity.numActividad)
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
                Text(text = pastActivity.fechaActividad)
            }
        }
    }
}

@Composable
fun PastActivityList() {
    val actividadesPasadas = listOf(
        PastActivity(
            numActividad = stringResource(id = R.string.actividad_pasada1),
            fechaActividad = stringResource(id = R.string.fecha_actividad)
        ),
        PastActivity(
            numActividad = stringResource(id = R.string.actividad_pasada2),
            fechaActividad = stringResource(id = R.string.fecha_actividad)
        ),
        PastActivity(
            numActividad = stringResource(id = R.string.actividad_pasada3),
            fechaActividad = stringResource(id = R.string.fecha_actividad)
        )
    )

    Column(modifier = Modifier) {
        actividadesPasadas.forEach { pastActivity ->
            PastActivityCard(pastActivity = pastActivity)
        }
    }
}

