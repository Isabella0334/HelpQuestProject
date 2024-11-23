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
import org.osmdroid.config.Configuration
import android.app.Application
import android.util.Log
import androidx.navigation.NavType
import androidx.navigation.navArgument

class MyApplication : Application() { // clase para crear el usuario osmdroid para la importación del mapa
    override fun onCreate() {
        super.onCreate()
        Configuration.getInstance().userAgentValue = "HelpQuest"  // seteado en el androidmanifest
    }
}

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

// La función que maneja la navegación en la aplicación
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun helpQuestNavegation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    // Obtener el nombre de la pantalla actual para manejar la barra de navegación
    val backStackEntry by navController.currentBackStackEntryAsState()
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
            startDestination = "login",  // Pantalla inicial, la de login
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = "login") {
                LoginScreen(navController = navController)  // Pantalla de login
            }
            composable(route = "Feed") {
                FeedScreen(navController = navController)  // Pantalla de feed
            }
            composable(
                route = "Info/{idA}", // Ruta que acepta el argumento idA
                arguments = listOf(navArgument("idA") { type = NavType.StringType }) // Especificar que idA es String
            ) { backStackEntry ->
                val idA = backStackEntry.arguments?.getString("idA") // Recuperar idA del argumento
                InfoScreen(navController = navController, idA = idA) // Pasar idA a InfoScreen
            }
            composable(
                route = "Formulario/{activityId}",
                arguments = listOf(navArgument("activityId") { type = NavType.StringType })
            ) { backStackEntry ->
                val activityId = backStackEntry.arguments?.getString("activityId") ?: ""
                if (activityId.isEmpty()) {
                    Log.e("Formulario", "El activityId es nulo o vacío")
                    return@composable
                }

                FormularioScreen(navController = navController, activityId = activityId)
            }


            composable(route = "Explore") {
                ExploreScreen(navController = navController)
            }
            composable(route = "Perfil") {
                PantallaPerfil(navController = navController)
            }
            composable(route = "register") {
                RegisterScreen(navController = navController)
            }
        }
    }
}

// Barra de navegación superior
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
