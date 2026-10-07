package com.example.chitaysostav.presentation.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.colorspace.WhitePoint
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chitaysostav.presentation.theme.AccentGreen
import com.example.chitaysostav.presentation.theme.AccentGreenDark

@Composable
fun HomeScreen(onScan: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        Row(modifier = Modifier.fillMaxWidth()) {

            Text(
                text = "Читай Состав",
                color = White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp
            )

        }
        ScanCard(onScan = onScan)
    }
}

@Composable
private fun ScanCard(onScan: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF166534),
                        Color(0xFF14532D),
                        Color(0xFF052E16)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height)
                )
            )
            drawCircle(
                color = Color(0x1F4ADE80),
                radius = 75.dp.toPx(),
                center = Offset(size.width - 45.dp.toPx(), 45.dp.toPx())
            )
            drawCircle(
                color = Color(0x1A4ADE80),
                radius = 50.dp.toPx(),
                center = Offset(size.width - 80.dp.toPx(), size.height)
            )
        }

        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "БЫСТРОЕ СКАНИРОВАНИЕ",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF86EFAC),
                letterSpacing = 1.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Проверь состав\nза 2 секунды",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Наведи камеру на штрихкод",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF86EFAC)
            )
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = onScan,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentGreen,
                    contentColor = AccentGreenDark
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(imageVector = Icons.Filled.QrCodeScanner, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Сканировать", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}