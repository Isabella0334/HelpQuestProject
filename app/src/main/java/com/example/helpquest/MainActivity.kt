package com.example.helpquest

import LoginScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.helpquest.ui.theme.HelpQuestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HelpQuestTheme {
                helpQuestNavegation()
            }
        }
    }
}

//El scaffold define la plantilla de la aplicación está acá
@OptIn(ExperimentalMaterial3Api::class)
//@Preview
@Composable
fun helpQuestNavegation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = backStackEntry?.destination?.route ?: "login"

    Scaffold(
        topBar = {
            AppBar(
                currentScreen,
                navController.previousBackStackEntry != null,
                navigateUp = {
                    navController.navigateUp()
                })
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,

            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ){
            composable(route = "login"){
                LoginScreen(navController = navController)
            }
            composable(route = "feed") {
                FeedScreen(navController = navController)
            }
            composable(route = "Info") {
                InfoScreen(navController = navController)
            }
            composable (route = "Formulario"){
                FormularioScreen(navController = navController)
            }
            composable (route = "Explore"){
                ExploreScreen(navController = navController)
            }
            composable (route = "Perfil"){
                PantallaPerfil(navController = navController)
            }
            composable (route = "explore"){
                ExploreScreen(navController = navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(title: String, canNavigateBack: Boolean, navigateUp: () -> Unit = {}, modifier: Modifier = Modifier) {
    TopAppBar(
        title = {
            Text(
                text = title
            ) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        modifier = modifier
    )
}