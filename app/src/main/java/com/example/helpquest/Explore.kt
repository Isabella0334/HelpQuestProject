package com.example.helpquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

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
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

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
                    text = stringResource(id = R.string.building_map),
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            }

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
                            onClick = { /*  More info */ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34C85A))
                        ) {
                            Text(stringResource(id = R.string.more_info), color = Color.White)
                        }

                        Button(
                            onClick = { /*  Apply */ },
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
