package com.example.helpquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
<<<<<<< HEAD

=======
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class Explore : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController() // conflictos con métodos y parámetro
            ExploreScreen(navController)
        }
    }
}
>>>>>>> 19747ae82a5ff094ad999cbda38d7c5ae3b00996

@Composable
fun ExploreScreen(navController: NavHostController) {
    Scaffold(
<<<<<<< HEAD
        bottomBar = { BottomNavigationBar(navController = navController) }
=======
        bottomBar = { BottomNavigationBar(navController) }
>>>>>>> 19747ae82a5ff094ad999cbda38d7c5ae3b00996
    ) { paddingValues ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5DC))
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            /*Icon(
                imageVector = Icons.Filled.ArrowBack, // regresar
                contentDescription = "Regresar",
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(16.dp)
            )*/ // testeando appbar

            // Tarjeta en espera de build map
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
                Text(
                    text = stringResource(id = R.string.exploretitle),
                    textAlign = TextAlign.Center ,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // mapsfunction()

            // Contenedor de info
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
                        text = "Title",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Body text for whatever you'd like to say. Add main takeaway points, quotes, anecdotes, or even a very very short story.",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { /*  More info */ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34C85A))
                        ) {
                            Text("More info", color = Color.White)
                        }

                        Button(
                            onClick = { /*  Apply */ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500))
                        ) {
                            Text("Apply", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    BottomAppBar(
        containerColor = Color(0xFF90EE90),

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(onClick = { navController.navigate("explore")}, // seteado
                colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)) {

                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "Explore",
                    modifier = Modifier.size(24.dp)
                )
                Text("Explore")

            }
            TextButton(onClick = { navController.navigate("perfil") }, // seteado
                colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(24.dp)
                        .clickable { navController.navigate ("Perfil") }
                )
                Text("Profile")
            }
            TextButton(onClick = { navController.navigate("Settings")}, // seteado
                colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)) {
                Column {

                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "Updates",
                        modifier = Modifier.size(24.dp)
                    )

                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("Updates")
            }
        }
    }
}

// en construcción para el mapa real
/*
@Composable
fun mapsfunction() {
}*/

<<<<<<< HEAD
=======
@Preview(showBackground = true)
@Composable
fun PreviewExploreScreen() {

    val navController = rememberNavController()
    NavHost(navController, startDestination = "explore") {
        composable("explore") { ExploreScreen(navController) }
    }
}
>>>>>>> 19747ae82a5ff094ad999cbda38d7c5ae3b00996
