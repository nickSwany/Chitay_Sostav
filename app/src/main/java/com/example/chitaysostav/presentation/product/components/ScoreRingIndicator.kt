package com.example.chitaysostav.presentation.product.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chitaysostav.domain.model.ScoreLevel
import com.example.chitaysostav.presentation.theme.ScoreBad
import com.example.chitaysostav.presentation.theme.ScoreGood
import com.example.chitaysostav.presentation.theme.ScoreMedium
import com.example.chitaysostav.presentation.theme.SurfaceVariant
import com.example.chitaysostav.presentation.theme.TextDisabled
import com.example.chitaysostav.presentation.theme.TextMuted

@Composable
fun ScoreRingIndicator(
    score: Int,
    modifier: Modifier = Modifier,
    size: Dp = 96.dp,
    isReliable: Boolean = true
) {
    val level = ScoreLevel.from(score)
    val arcColor = level.toColor()

    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(size)) {
            val strokeWidth = 8.dp.toPx()
            drawArc(
                color = SurfaceVariant,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            drawArc(
                color = arcColor,
                startAngle = -90f,
                sweepAngle = 360f * score.coerceIn(0, 100) / 100f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = if(isReliable)"$score" else "~$score",
                color = arcColor,
                fontSize = if (isReliable)24.sp else 20.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 24.sp
            )
            Text(
                text = "/100",
                color = TextDisabled,
                fontSize = 11.sp,
                lineHeight = 13.sp
            )
        }
    }
}

@Composable
fun NoScoreRingIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 96.dp
) {
    val dashColor = Color(0xFF3F3F46)
    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(size)) {
            val strokeWidth = 8.dp.toPx()
            drawArc(
                color = SurfaceVariant,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            var angle = -90f
            while (angle < 270f) {
                drawArc(
                    color = dashColor,
                    startAngle = angle,
                    sweepAngle = 15f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
                angle += 35f
            }
        }
        Text(
            text = "?",
            color = TextMuted,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            lineHeight = 28.sp
        )
    }
}

fun ScoreLevel.toColor(): Color = when (this) {
    ScoreLevel.GOOD -> ScoreGood
    ScoreLevel.AVERAGE -> ScoreMedium
    ScoreLevel.BAD -> ScoreBad
}

fun ScoreLevel.toDimColor(): Color = when (this) {
    ScoreLevel.GOOD -> Color(0x1F4ADE80)
    ScoreLevel.AVERAGE -> Color(0x1FFACC15)
    ScoreLevel.BAD -> Color(0x1FF87171)
}