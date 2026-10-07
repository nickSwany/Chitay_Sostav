package com.example.chitaysostav.presentation.product

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chitaysostav.R
import com.example.chitaysostav.presentation.theme.AccentGreen
import com.example.chitaysostav.presentation.theme.AccentGreenDark
import com.example.chitaysostav.presentation.theme.Background
import com.example.chitaysostav.presentation.theme.Border
import com.example.chitaysostav.presentation.theme.ChitaySostavTheme
import com.example.chitaysostav.presentation.theme.TextMuted

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NotFoundScreenPreview() {
    ChitaySostavTheme {
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            NotFoundScreen(onScanAgain = {}, onAddProduct = {})
        }
    }
}

@Composable
fun NotFoundScreen(onScanAgain: () -> Unit, onAddProduct: () -> Unit = {}) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Icon(
                painter = painterResource(id = R.drawable.ic_not_found),
                tint = TextMuted,
                contentDescription = "",
                modifier = Modifier.size(96.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Товар не найден",
                color = Color.White,
                fontSize = 20.sp,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Этого товара пока нет в нашей базе. Помогите сообществу — добавьте его!",
                color = TextMuted,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onAddProduct,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentGreen,
                    contentColor = AccentGreenDark
                )
            ) {
                Text("✏️ Добавить товар", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(4.dp))

            OutlinedButton(
                onClick = onScanAgain,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Background,
                    contentColor = TextMuted
                ),
                border = BorderStroke(1.dp, Border)
            ) {
                Text("Сканировать снова", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}