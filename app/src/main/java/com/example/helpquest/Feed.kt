package com.example.helpquest

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.helpquest.R

@Composable
fun CustomCard() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8EFE8))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color(0xFFF8EFE8)),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Image(
                    painter = painterResource(id = R.drawable.img_tarjeta1), // Imagen de prueba
                    contentDescription = "Imagen de ejemplo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Crop
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    LabelChip(label = "❤\uFE0F Comunidad", color = Color(0xFF9C27B0)) // Amarillo
                    LabelChip(label = "\uD83D\uDCAA Equipo", color = Color(0xFF4CAF50)) // Verde
                    LabelChip(label = "\uD83C\uDF31 Medio Ambiente", color = Color(0xFF2196F3)) // Azul
                }

                Text(
                    text = "⏰ 4h - 1Día",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Text(
                    text = "Reforestación Comunitaria",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Únete a una jornada de reforestación en el parque ecológico. Ayudaremos a plantar más de 100 árboles y aprenderás sobre la importancia de los ecosistemas locales. ¡Tu participación es crucial para el medio ambiente!",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Button(
                        onClick = { /* Acción para More info */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text(text = "Más información")
                    }
                    Button(
                        onClick = { /* Acción para Aplicar */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
                    ) {
                        Text(text = "Aplicar")
                    }

                }
            }
        }

    }
}

@Composable
fun LabelChip(label: String, color: Color) {
    Surface(
        color = color,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = Color.White,
            fontSize = 11.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomCard() {
    CustomCard()
}
