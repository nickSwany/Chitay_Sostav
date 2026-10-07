package com.example.chitaysostav.presentation.product.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chitaysostav.domain.model.Additive
import com.example.chitaysostav.domain.model.RiskLevel
import com.example.chitaysostav.presentation.theme.Background
import com.example.chitaysostav.presentation.theme.CardSurface
import com.example.chitaysostav.presentation.theme.ChitaySostavTheme
import com.example.chitaysostav.presentation.theme.ScoreBad
import com.example.chitaysostav.presentation.theme.ScoreGood
import com.example.chitaysostav.presentation.theme.ScoreMedium
import com.example.chitaysostav.presentation.theme.TextMuted

@Composable
fun AdditiveChip(additive: Additive) {

    Column(
        modifier = Modifier
            .background( CardSurface)
            .fillMaxWidth()
            .padding(horizontal = 0.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(7.dp)
                    .background(additive.risk.toColor(), CircleShape)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = "${additive.eNumber.uppercase()} - ${additive.name}",
                color = Color.White,
                fontSize = 13.sp,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            modifier = Modifier.padding(start = 13.dp),
            text = additive.description,
            color = TextMuted,
            fontSize = 11.sp,
            lineHeight = 14.sp,
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(6.dp))

        HorizontalDivider()

    }
}

fun RiskLevel.toColor(): Color {
    return when (this) {
        RiskLevel.SAFE -> ScoreGood
        RiskLevel.WARN -> ScoreMedium
        RiskLevel.DANGER -> ScoreBad
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AdditiveChipPreview() {
    ChitaySostavTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AdditiveChip(
                Additive(
                    "e300", "Аскорбиновая кислота", RiskLevel.SAFE,
                    emptyList(), "Витамин C. Природный антиоксидант, укрепляет иммунитет."
                )
            )
            AdditiveChip(
                Additive(
                    "e322",
                    "Лецитин соевый",
                    RiskLevel.WARN,
                    emptyList(),
                    "Эмульгатор из сои. Считается безопасным, но может вызвать аллергию."
                )
            )
            AdditiveChip(
                Additive(
                    "e250",
                    "Нитрит натрия",
                    RiskLevel.DANGER,
                    emptyList(),
                    "Консервант в колбасах. При жарке образует канцерогенные нитрозамины."
                )
            )
        }
    }
}