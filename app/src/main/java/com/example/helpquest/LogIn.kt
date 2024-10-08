import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.example.helpquest.R

@Composable
fun LoginScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
            onClick = { navController.navigate("feed") },
            colors = ButtonDefaults.buttonColors(Color(0xFFFFA500)),
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
                .height(40.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = stringResource(id = R.string.login_button_text), color = Color.White, fontSize = 18.sp)
        }

        // Register Button
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(Color(0xFF00C853)),
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
                .height(40.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = stringResource(id = R.string.register_button_text), color = Color.White, fontSize = 18.sp)
        }
    }
}

