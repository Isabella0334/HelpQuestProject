package com.example.helpquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController

class Explore : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExploreScreen()
        }
    }
}

@Composable
fun ExploreScreen() {
    Scaffold(
        bottomBar = { BottomNavigationBar() }
    ) { paddingValues ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5DC))
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Icon(
                imageVector = Icons.Filled.ArrowBack, // regresar
                contentDescription = "Regresar",
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(16.dp)
            )

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
                    text = "Building MAP",
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
fun BottomNavigationBar() {
    BottomAppBar(
        containerColor = Color(0xFF90EE90),

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(onClick = { /*  Explore */ },
                colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)) {

                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "Explore",
                    modifier = Modifier.size(24.dp)
                )
                Text("Explore")
            }
            TextButton(onClick = { /* Profile */ },
                colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(24.dp)
                )
                Text("Profile")
            }
            TextButton(onClick = { /* Updates */ },
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

@Composable
fun PreviewExploreScreen(navController: NavHostController) {
    ExploreScreen()
}
