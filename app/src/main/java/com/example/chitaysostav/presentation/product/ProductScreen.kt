package com.example.chitaysostav.presentation.product

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.chitaysostav.R
import com.example.chitaysostav.domain.model.Product
import com.example.chitaysostav.domain.model.Additive
import com.example.chitaysostav.domain.model.ProductCategory
import com.example.chitaysostav.domain.model.ProductScore
import com.example.chitaysostav.domain.model.RiskLevel
import com.example.chitaysostav.domain.model.ScoreLevel
import com.example.chitaysostav.presentation.components.LoadingContent
import com.example.chitaysostav.presentation.product.components.NoScoreRingIndicator
import com.example.chitaysostav.presentation.product.components.ScoreRingIndicator
import com.example.chitaysostav.presentation.product.components.toColor
import com.example.chitaysostav.presentation.product.components.toDimColor
import com.example.chitaysostav.presentation.product.components.AdditiveChip
import com.example.chitaysostav.presentation.product.components.InfoChipCard
import com.example.chitaysostav.presentation.product.components.formatNutriment
import com.example.chitaysostav.presentation.theme.AccentGreen
import com.example.chitaysostav.presentation.theme.AccentGreenDark
import com.example.chitaysostav.presentation.theme.BackgroundBack
import com.example.chitaysostav.presentation.theme.Border
import com.example.chitaysostav.presentation.theme.CardSurface
import com.example.chitaysostav.presentation.theme.ChitaySostavTheme
import com.example.chitaysostav.presentation.theme.SurfaceVariant
import com.example.chitaysostav.presentation.theme.TextMuted
import com.example.chitaysostav.presentation.theme.TextPrimary

@Composable
fun ProductScreen(
    barcode: String,
    onScanAgain: () -> Unit,
    onEdit: () -> Unit,
    onBack: () -> Unit
) {
    val viewModel: ProductViewModel = hiltViewModel<ProductViewModel, ProductViewModel.Factory>(

    ) { factory -> factory.create(barcode) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
//            .statusBarsPadding()
    ) {
        when (uiState) {
            is ProductUiState.Loading -> LoadingContent()
            is ProductUiState.NotFound -> NotFoundScreen(
                onScanAgain = onScanAgain,
                onAddProduct = onEdit
            )

            is ProductUiState.Success -> {
                val success = uiState as ProductUiState.Success
                ProductContent(
                    product = success.product,
                    additive = success.additives,
                    score = success.score,
                    onScanAgain = onScanAgain,
                    onEdit = onEdit,
                    onBack = onBack,
                    onShare = { viewModel.shareProduct(product = success.product) }
                )
            }
        }
    }
}

@Composable
private fun ProductContent(
    product: Product,
    additive: List<Additive>,
    score: ProductScore?,
    onScanAgain: () -> Unit,
    onEdit: () -> Unit,
    onBack: () -> Unit,
    onShare: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(Color(0xFF141414), RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
            ) {

                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 12.dp, start = 16.dp)
                        .size(36.dp)
                        .background(BackgroundBack, CircleShape)
                        .clickable(onClick = onBack),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 12.dp, end = 16.dp)
                        .size(36.dp)
                        .background(BackgroundBack, CircleShape)
                        .clickable(onClick = onShare),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_share), // Замнеить на поделиться
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                if (product.emoji.isNotEmpty()) {
                    Text(
                        product.emoji,
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 80.sp
                    )
                } else {
                    Text(
                        ProductCategory.OTHER.emoji,
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 80.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            val scoreValue = score?.score
            val scoreLevel = scoreValue?.let { ScoreLevel.from(it) }
            var showDisclaimer by remember { mutableStateOf(false) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (scoreValue != null) {
                    ScoreRingIndicator(
                        score = scoreValue,
                        modifier = Modifier.padding(end = 16.dp),
                        isReliable = score?.isReliable ?: true
                    )
                } else {
                    NoScoreRingIndicator(
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    if (scoreLevel != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .background(scoreLevel.toDimColor(), RoundedCornerShape(20.dp))
                                    .padding(horizontal = 12.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .background(scoreLevel.toColor(), CircleShape)
                                )
                                Text(
                                    text = scoreLevel.label,
                                    color = scoreLevel.toColor(),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .border(1.5.dp, Color(0xFF3F3F46), CircleShape)
                                    .clickable { showDisclaimer = true },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "!",
                                    color = TextMuted,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 11.sp
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "Недостаточно данных",
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    }

                    Text(
                        text = product.name.ifEmpty { stringResource(R.string.no_name) },
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 21.sp
                    )

                    if (product.brands.isNotEmpty()) {
                        Text(
                            text = "${product.brands} · 100 г",
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            if (showDisclaimer) {
                AlertDialog(
                    onDismissRequest = { showDisclaimer = false },
                    containerColor = Color(0xFF1C1C1F),
                    shape = RoundedCornerShape(20.dp),
                    titleContentColor = Color.White,
                    textContentColor = TextMuted,
                    title = {
                        Text(
                            "Об оценке товара",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    text = {
                        Text(
                            text = "Оценка рассчитывается по алгоритму NutriScore на основе калорийности, содержания сахара, насыщенных жиров, соли, белков и клетчатки.\n\nЕсли данные о питательной ценности не заполнены — оценка не отображается.\n\nЗначок ~ означает, что оценка приблизительная: в составе обнаружен сахар или соль, но их точное количество на этикетке не указано.\n\nОценка носит ориентировочный характер и не является медицинской рекомендацией.",
                            fontSize = 13.sp,
                            lineHeight = 19.sp
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = { showDisclaimer = false }, shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AccentGreen,
                                contentColor = AccentGreenDark
                            )
                        ) {
                            Text("Понятно", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                InfoChipCard(
                    "Калории",
                    formatNutriment(product.nutriments, "energy-kcal_100g", ""),
                    Modifier.weight(1f)
                )
                InfoChipCard(
                    "Белки",
                    formatNutriment(product.nutriments, "proteins_100g", "г"),
                    Modifier.weight(1f)
                )
                InfoChipCard(
                    "Жиры",
                    formatNutriment(product.nutriments, "fat_100g", "г"),
                    Modifier.weight(1f)
                )
                InfoChipCard(
                    "Углев.",
                    formatNutriment(product.nutriments, "carbohydrates_100g", "г"),
                    Modifier.weight(1f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardSurface, RoundedCornerShape(14.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = "Состав:",
                    color = Color.White,
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                if (product.ingredients.isNotEmpty()) {

                    Text(
                        text = product.ingredients,
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall,
                    )
                } else {
                    Text(
                        text = "Состав пока не заполнен. Вы можете помочь и добавить его!",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            if (additive.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .background(CardSurface, RoundedCornerShape(14.dp))
                        .padding(12.dp)
                ) {

                    Text(
                        text = "Список добавок:",
                        color = Color.White,
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        additive.forEach {
                            AdditiveChip(it)
                        }
                    }
                }
            }

            Spacer(Modifier.height(148.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, MaterialTheme.colorScheme.background)
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = SurfaceVariant,
                        contentColor = TextPrimary
                    ),
                    border = BorderStroke(1.dp, Border)
                ) {
                    Text("✏\uFE0F Редактировать", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
                Button(
                    onClick = onScanAgain,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentGreen,
                        contentColor = AccentGreenDark
                    )
                ) {
                    Text(
                        stringResource(R.string.scan_again),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ProductContentPreview() {
    ChitaySostavTheme {
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
        ) {
            ProductContent(
                product = Product(
                    name = "Шоколад молочный «Алёнка»",
                    brands = "Красный Октябрь",
                    ingredients = "Сахар, какао-масло, молоко сухое цельное, какао тёртое, лактоза, молоко сухое обезжиренное, эмульгатор (лецитин соевый), ароматизатор (ванилин).",
                    image = "",
                    emoji = "🍫",
                    additives = listOf("e322", "e150d", "e250"),
                    nutriments = mapOf(
                        "energy-kcal_100g" to 544.0,
                        "proteins_100g" to 7.8,
                        "fat_100g" to 30.5,
                        "carbohydrates_100g" to 59.4
                    )
                ),
                additive = listOf(
                    Additive(
                        "e322", "Лецитин соевый", RiskLevel.WARN, emptyList(),
                        "Эмульгатор из сои. Считается безопасным, но может вызвать аллергию."
                    ),
                    Additive(
                        "e150d", "Сахарный колер IV", RiskLevel.WARN, emptyList(),
                        "Карамельный краситель. Содержит 4-МИ."
                    ),
                    Additive(
                        "e250", "Нитрит натрия", RiskLevel.DANGER, emptyList(),
                        "Консервант в колбасах. При жарке образует канцерогенные нитрозамины."
                    )
                ),
                score = null,
                onScanAgain = {},
                onEdit = {},
                onBack = {},
                onShare = {}
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Без эмодзи",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ProductContentNoEmojiPreview() {
    ChitaySostavTheme {
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
        ) {
            ProductContent(
                product = Product(
                    name = "Неизвестный товар",
                    brands = "",
                    ingredients = "",
                    image = "",
                    emoji = ""
                ),
                additive = emptyList(),
                score = null,
                onScanAgain = {},
                onEdit = {},
                onBack = {},
                onShare = {}
            )
        }
    }
}