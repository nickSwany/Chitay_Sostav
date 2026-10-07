package com.example.chitaysostav.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.chitaysostav.domain.model.ScannedProduct
import com.example.chitaysostav.domain.model.ProductCategory
import com.example.chitaysostav.presentation.history.viewmodel.HistoryViewModel
import com.example.chitaysostav.presentation.theme.Background
import com.example.chitaysostav.presentation.theme.Border
import com.example.chitaysostav.presentation.theme.TextDisabled
import com.example.chitaysostav.presentation.theme.TextMuted
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(onProductHistoryClick: (String) -> Unit) {

    val viewModel: HistoryViewModel = hiltViewModel()
    val uiState by viewModel.history.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 20.dp, end = 20.dp, bottom = 8.dp)
        ) {
            Text(
                text = "История",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp,
                modifier = Modifier.align(Alignment.BottomStart)
            )
            if (uiState is HistoryUiState.Success && (uiState as HistoryUiState.Success).products.isNotEmpty()) {
                Text(
                    text = "Очистить",
                    color = Color.Red,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .clickable { viewModel.clearHistory() }
                        .padding(horizontal = 4.dp)
                )
            }
        }

        // Контент
        when (uiState) {
            HistoryUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is HistoryUiState.Success -> {
                val products = (uiState as HistoryUiState.Success).products
                if (products.isNotEmpty()) {
                    HistoryContent(products, onProductHistoryClick = onProductHistoryClick)
                } else {
                    HistoryEmpty()
                }
            }
        }
    }
}

@Composable
private fun HistoryContent(
    products: List<ScannedProduct>,
    onProductHistoryClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 4.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        val grouped = products.groupBy { dataKey(it.scannedAt) }

        grouped.forEach { (dateLabel, groupItems) ->
            item {
                Text(
                    text = dateLabel,
                    color = TextMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.8.sp,
                    modifier = Modifier.padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 4.dp,
                        bottom = 8.dp
                    )
                )
            }
            itemsIndexed(groupItems) { index, product ->
                HistoryCard(product = product, onClick = {
                    onProductHistoryClick(product.barcode)
                })
                if (index < groupItems.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        color = Border,
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryCard(product: ScannedProduct, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color(0xFF141414), RoundedCornerShape(14.dp))
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, Border, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                product.emoji.ifEmpty { ProductCategory.OTHER.emoji },
                fontSize = 24.sp
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = product.name,
                fontSize = 14.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = product.brands,
                fontSize = 12.sp,
                color = TextMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Text(
            text = remember(product.scannedAt) {
                SimpleDateFormat("HH:mm", Locale.getDefault())
                    .format(Date(product.scannedAt))
            },
            fontSize = 11.sp,
            color = TextDisabled
        )
    }
}

private fun dataKey(scannedAt: Long): String {
    val now = Calendar.getInstance()
    val date = Calendar.getInstance().apply { timeInMillis = scannedAt }
    return when {
        isSameDay(now, date) -> "Сегодня"
        isYesterdayDay(date) -> "Вчера"
        else -> SimpleDateFormat("d MMMM", Locale.getDefault()).format(Date(scannedAt))
    }
}

private fun isSameDay(a: Calendar, b: Calendar): Boolean {
    return a.get(Calendar.YEAR) == b.get(Calendar.YEAR) &&
            a.get(Calendar.MONTH) == b.get(Calendar.MONTH) &&
            a.get(Calendar.DAY_OF_MONTH) == b.get(Calendar.DAY_OF_MONTH)
}

private fun isYesterdayDay(b: Calendar): Boolean {
    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
    return isSameDay(yesterday, b)
}

@Composable
private fun HistoryEmpty() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.History,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Пока ничего нет",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Отсканируйте первый товар, и он появится здесь",
            color = TextMuted,
            fontSize = 13.sp,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF111113)
@Composable
private fun HistoryCardPreview() {
    HistoryCard(
        product = ScannedProduct(
            barcode = "123",
            name = "Молоко Простоквашино",
            brands = "Простоквашино",
            image = "",
            emoji = "🥛",
            scannedAt = 1_719_306_600_000L
        ), onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF111113)
@Composable
private fun HistoryEmptyPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("История", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
        HistoryEmpty()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF111113)
@Composable
private fun HistoryListPreview() {
    val now = System.currentTimeMillis()
    val yesterday = now - 86_400_000L
    val fakeProducts = listOf(
        ScannedProduct(
            barcode = "123",
            name = "Молоко Простоквашино",
            brands = "Простоквашино",
            image = "",
            emoji = "🥛",
            scannedAt = now
        ),
        ScannedProduct(
            barcode = "456",
            name = "Шоколад Alpen Gold",
            brands = "Mondelez",
            image = "",
            emoji = "🍫",
            scannedAt = now - 3_600_000L
        ),
        ScannedProduct(
            barcode = "789",
            name = "Кефир Bio Баланс",
            brands = "Данон",
            image = "",
            emoji = "🥛",
            scannedAt = yesterday
        ),
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "История",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp
            )
            Text("Очистить", color = Color.Red, fontSize = 13.sp)
        }
        HistoryContent(products = fakeProducts, onProductHistoryClick = {})
    }
}