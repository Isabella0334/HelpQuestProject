import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.example.helpquest.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }  // Mensaje de error
    val auth = FirebaseAuth.getInstance()  // Instancia de FirebaseAuth
    val snackbarHostState = remember { SnackbarHostState() }  // Snackbar para mostrar errores

    // Cuando errorMessage cambia, mostramos el Snackbar
    LaunchedEffect(errorMessage) {
        if (errorMessage.isNotBlank()) {
            snackbarHostState.showSnackbar(errorMessage)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5E9DF))
            .padding(horizontal = 50.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.email_label),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.Start)
        )

        // Email Text Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = stringResource(id = R.string.email_label)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        )

        Text(
            text = stringResource(id = R.string.password_label),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 10.dp)
        )

        // Password Text Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = stringResource(id = R.string.password_label)) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        )

        // Log in Button
        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    // Intentamos iniciar sesión con Firebase Authentication
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Si el login es exitoso, redirigir a la pantalla "feed"
                                navController.navigate("feed")
                            } else {
                                // Si hay un error, mostrar mensaje en snackbar
                                errorMessage = task.exception?.message ?: "Error desconocido"
                            }
                        }
                } else {
                    errorMessage = "Por favor ingresa un correo y una contraseña"
                }
            },
            colors = ButtonDefaults.buttonColors(Color(0xFFFFA500)),
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
                .height(40.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = stringResource(id = R.string.login_button_text), color = Color.White, fontSize = 18.sp)
        }

        // Register Button (Aún no funcional)
        Button(
            onClick = { navController.navigate("register") },
            colors = ButtonDefaults.buttonColors(Color(0xFF00C853)),
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
                .height(40.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = stringResource(id = R.string.register_button_text), color = Color.White, fontSize = 18.sp)
        }

        // Snackbar para mostrar errores
        SnackbarHost(
            hostState = snackbarHostState
        )
    }
}
