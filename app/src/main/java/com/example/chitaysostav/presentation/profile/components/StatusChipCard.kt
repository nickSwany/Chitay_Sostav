package com.example.chitaysostav.presentation.profile.components

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
import com.example.chitaysostav.presentation.theme.AccentGreen
import com.example.chitaysostav.presentation.theme.CardSurface
import com.example.chitaysostav.presentation.theme.TextDisabled
import java.util.Locale

@Composable
fun StatusChipCard(name: String, count: String, modifier: Modifier) {
    Column(
        modifier = modifier
            .background(CardSurface, RoundedCornerShape(14.dp))
            .padding(6.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = count,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = AccentGreen
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = name.uppercase(),
            fontSize = 10.sp,
            color = TextDisabled,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp
        ) // Почему тут getDefault
    }
}