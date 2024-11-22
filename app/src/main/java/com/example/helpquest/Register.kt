package com.example.helpquest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth

private const val s = "Usuario registrado exitosamente."

@Composable
fun RegistrationScreen(
    navController: NavHostController, // Recibimos el navController aquí
    onRegister: () -> Unit
) {
    // Estados para los campos
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var contactInfo by remember { mutableStateOf("") } // Email
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var termsAccepted by remember { mutableStateOf(false) }
    // Validaciones
    var emailError by remember { mutableStateOf("") }
    var birthDateError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }

    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }


    // Función para validar correo electrónico
    fun validateEmail(): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}"
        emailError = if (!contactInfo.matches(emailPattern.toRegex())) {
            "Por favor ingresa un correo válido"
        } else {
            ""
        }
        return emailError.isEmpty()
    }

    // Función para validar la fecha de nacimiento
    fun validateBirthDate(): Boolean {
        val datePattern = "^\\d{2}-\\d{2}-\\d{4}$"  // Expresión regular para formato "yyyy-MM-dd"
        birthDateError = if (!birthDate.matches(datePattern.toRegex())) {
            "Por favor ingresa una fecha válida (dd-MM-yyyy)"
        } else {
            ""
        }
        return birthDateError.isEmpty()
    }

    // Función para validar la contraseña
    fun validatePassword(): Boolean {
        passwordError = when {
            password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            !password.any { it.isDigit() } -> "La contraseña debe contener al menos un número"
            !password.any { it.isLetter() } -> "La contraseña debe contener al menos una letra"
            else -> ""
        }

        return passwordError.isEmpty()
    }

    // Función para validar la confirmación de la contraseña
    fun validateConfirmPassword(): Boolean {
        confirmPasswordError = if (password != confirmPassword) {
            "Las contraseñas no coinciden"
        } else {
            ""
        }
        return confirmPasswordError.isEmpty()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8EFE8))
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título
        Text(
            text = stringResource(id = R.string.registration_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .padding(bottom = 24.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        // Campos del formulario
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text(stringResource(id = R.string.label_first_name)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text(stringResource(id = R.string.label_last_name)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de fecha de nacimiento con validación
        OutlinedTextField(
            value = birthDate,
            onValueChange = { birthDate = it },
            label = { Text(stringResource(id = R.string.label_birth_date)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = birthDateError.isNotEmpty()
        )
        if (birthDateError.isNotEmpty()) {
            Text(
                text = birthDateError,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de correo electrónico con validación
        OutlinedTextField(
            value = contactInfo,
            onValueChange = { contactInfo = it; emailError = "" }, // Limpiar error al cambiar valor
            label = { Text(stringResource(id = R.string.label_email)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = emailError.isNotEmpty()
        )
        if (emailError.isNotEmpty()) {
        Text(
            text = emailError,
            color = Color.Red,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 16.dp)
        )
    }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(id = R.string.label_password)) },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            isError = passwordError.isNotEmpty(),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (isPasswordVisible)
                            stringResource(id = R.string.password_hide)
                        else
                            stringResource(id = R.string.password_show)
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        if (passwordError.isNotEmpty()) {
            Text(
                text = passwordError,
                color = Color.Red,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.Start)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de confirmación de contraseña
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text(stringResource(id = R.string.label_confirm_password)) },
            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            isError = confirmPasswordError.isNotEmpty(),
            trailingIcon = {
                IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                    Icon(
                        imageVector = if (isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (isConfirmPasswordVisible)
                            stringResource(id = R.string.password_hide)
                        else
                            stringResource(id = R.string.password_show)
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        if (confirmPasswordError.isNotEmpty()) {
            Text(
                text = confirmPasswordError,
                color = Color.Red,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.Start)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = termsAccepted,
                onCheckedChange = { termsAccepted = it }
            )
            Text(
                text = stringResource(id = R.string.label_terms_conditions),
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (termsAccepted && validateEmail() && validateBirthDate() && validatePassword() && validateConfirmPassword()) {                    // Aquí va el código para registrar al usuario en Firebase Authentication
                    FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(contactInfo, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // El registro fue exitoso
                                println("Usuario registrado exitosamente.")
                                // Navegar a la pantalla feed después de un registro exitoso
                                navController.navigate("login") // Pues que regrese al login para que ahora si pueda iniciar sesion
                            } else {
                                // Si el registro falla
                                println("Error al registrar usuario: ${task.exception?.message}")
                            }
                        }
                } else {
                    println("Errores en el formulario")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = firstName.isNotEmpty() && lastName.isNotEmpty() &&
                    birthDate.isNotEmpty() && contactInfo.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && termsAccepted
        ) {
            Text(text = stringResource(id = R.string.btn_register))
        }
    }
}

@Composable
fun RegisterScreen(navController: NavHostController) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8EFE8))
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            RegistrationScreen(navController = navController, onRegister = {
                // Navegación se maneja directamente dentro de RegistrationScreen
            })
        }
    }
}




