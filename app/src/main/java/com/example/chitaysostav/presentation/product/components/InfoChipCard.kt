package com.example.chitaysostav.presentation.product.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chitaysostav.presentation.theme.CardSurface
import com.example.chitaysostav.presentation.theme.TextDisabled
import com.example.chitaysostav.presentation.theme.TextPrimary
import java.util.Locale.getDefault

@Composable
fun InfoChipCard(name: String, count: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(CardSurface, RoundedCornerShape(14.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = name.uppercase(getDefault()),
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp,
            color = TextDisabled
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = count,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
    }
}

fun formatNutriment(map: Map<String, Any>, key: String, unit: String): String {
    val value = map[key] ?: return "-"
    val num = value.toString().toDoubleOrNull() ?: return "-"
    return if (num % 1.0 == 0.0) "${num.toInt()} $unit" else "$num $unit"
}
