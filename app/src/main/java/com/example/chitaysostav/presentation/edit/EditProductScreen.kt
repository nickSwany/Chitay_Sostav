package com.example.chitaysostav.presentation.edit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.chitaysostav.domain.model.ProductCategory
import com.example.chitaysostav.presentation.components.LoadingContent
import com.example.chitaysostav.presentation.edit.viewModel.EditProductViewModel
import com.example.chitaysostav.presentation.edit.viewModel.EditProductViewModelFactory
import com.example.chitaysostav.presentation.theme.AccentGreen
import com.example.chitaysostav.presentation.theme.AccentGreenDark
import com.example.chitaysostav.presentation.theme.Background
import com.example.chitaysostav.presentation.theme.Border
import com.example.chitaysostav.presentation.theme.CardSurface
import com.example.chitaysostav.presentation.theme.TextMuted
import com.example.chitaysostav.presentation.theme.TextPrimary

@Composable
fun EditProductScreen(
    barcode: String,
    onSaved: () -> Unit,
    onBack: () -> Unit
) {
    val viewModel: EditProductViewModel =
        hiltViewModel<EditProductViewModel, EditProductViewModelFactory>(

        ) { factory -> factory.create(barcode) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showExitDialog by remember { mutableStateOf(false) }
    var showOcrCamera by remember { mutableStateOf(false) }

    BackHandler {
        if (viewModel.hasChanges()) {
            showExitDialog = true
        } else {
            onBack()
        }
    }

    LaunchedEffect(uiState) {
        when (val s = uiState) {
            is EditProductUiState.Saved -> onSaved()
            is EditProductUiState.Error -> snackbarHostState.showSnackbar(s.message)
            else -> Unit
        }
    }
    if (showOcrCamera) {
        OcrCameraScreen(
            onTextRecognized = { text ->
                viewModel.ingredients.value = text
                showOcrCamera = false
            }, onClose = { showOcrCamera = false }
        )
    } else {
        Scaffold(
            contentWindowInsets = WindowInsets(0),
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Background)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            if (viewModel.hasChanges()) {
                                showExitDialog = true
                            } else {
                                onBack()
                            }
                        }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Назад",
                                tint = Color.White
                            )
                        }
                        Text(
                            text = "Редактировать",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f)
                        )
                        if (uiState is EditProductUiState.Saving) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(end = 16.dp),
                                color = AccentGreen,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Button(
                                onClick = { viewModel.save() },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AccentGreen,
                                    contentColor = AccentGreenDark
                                ),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Text("Сохранить", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                    }
                    HorizontalDivider(color = Color(0xFF1C1C1E))
                }
            },

            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { padding ->
            when (uiState) {
                is EditProductUiState.Loading -> LoadingContent()
                else -> EditForm(
                    viewModel = viewModel,
                    isSaving = uiState is EditProductUiState.Saving,
                    onOcrClick = { showOcrCamera = true },
                    modifier = Modifier.padding(padding),
                )
            }
        }

        if (showExitDialog) {
            AlertDialog(
                onDismissRequest = { showExitDialog = false },
                containerColor = Color(0xFF1C1C1F),
                shape = RoundedCornerShape(20.dp),
                titleContentColor = Color.White,
                textContentColor = TextMuted,
                title = {
                    Text(
                        text = "Несохранённые изменения",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = "Вы изменили данные товара. Сохранить изменения перед выходом?",
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 0.dp)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { viewModel.save() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentGreen,
                            contentColor = AccentGreenDark
                        )
                    ) {
                        Text("Сохранить", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = { showExitDialog = false; onBack() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = TextPrimary
                        ),
                        border = BorderStroke(1.dp, Border)
                    ) {
                        Text("Не сохранять", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            )
        }
    }
}

@Composable
private fun EditForm(
    viewModel: EditProductViewModel,
    isSaving: Boolean,
    onOcrClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    EditFormContent(
        name = viewModel.name.collectAsState().value,
        onNameChange = { viewModel.name.value = it },
        brands = viewModel.brands.collectAsState().value,
        onBrandsChange = { viewModel.brands.value = it },
        ingredients = viewModel.ingredients.collectAsState().value,
        onIngredientsChange = { viewModel.ingredients.value = it },
        emoji = viewModel.emoji.collectAsState().value,
        onEmojiChange = { viewModel.emoji.value = it },
        energyKcal = viewModel.energyKcal.collectAsState().value,
        onEnergyKcalChange = { viewModel.energyKcal.value = it },
        proteins = viewModel.proteins.collectAsState().value,
        onProteinsChange = { viewModel.proteins.value = it },
        fat = viewModel.fat.collectAsState().value,
        onFatChange = { viewModel.fat.value = it },
        saturatedFat = viewModel.saturatedFat.collectAsState().value,
        onSaturatedFatChange = { viewModel.saturatedFat.value = it },
        carbs = viewModel.carbs.collectAsState().value,
        onCarbsChange = { viewModel.carbs.value = it },
        sugars = viewModel.sugars.collectAsState().value,
        onSugarsChange = { viewModel.sugars.value = it },
        fiber = viewModel.fiber.collectAsState().value,
        onFiberChange = { viewModel.fiber.value = it },
        salt = viewModel.salt.collectAsState().value,
        onSaltChange = { viewModel.salt.value = it },
        additives = viewModel.additives.collectAsState().value,
        onAdditivesChange = { viewModel.additives.value = it },
        isSaving = isSaving,
        onSave = { viewModel.save() },
        onOcrClick = onOcrClick,
        modifier = modifier,
    )
}

@Composable
private fun EditFormContent(
    name: String, onNameChange: (String) -> Unit,
    brands: String, onBrandsChange: (String) -> Unit,
    ingredients: String, onIngredientsChange: (String) -> Unit,
    emoji: String, onEmojiChange: (String) -> Unit,
    energyKcal: String, onEnergyKcalChange: (String) -> Unit,
    proteins: String, onProteinsChange: (String) -> Unit,
    fat: String, onFatChange: (String) -> Unit,
    saturatedFat: String, onSaturatedFatChange: (String) -> Unit,
    carbs: String, onCarbsChange: (String) -> Unit,
    sugars: String, onSugarsChange: (String) -> Unit,
    fiber: String, onFiberChange: (String) -> Unit,
    salt: String, onSaltChange: (String) -> Unit,
    additives: String, onAdditivesChange: (String) -> Unit,
    isSaving: Boolean,
    onSave: () -> Unit,
    onOcrClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionLabel("Категория товара ")

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 0.dp)
        ) {
            items(ProductCategory.entries) {
                Column(
                    modifier = Modifier.width(56.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val isSelected = it.emoji == emoji
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFF141414), RoundedCornerShape(14.dp))
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { onEmojiChange(it.emoji) }
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) AccentGreen else Border,
                                RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            it.emoji,
                            fontSize = 24.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = it.label,
                        color = TextMuted,
                        fontSize = 10.sp,
                        lineHeight = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        HorizontalDivider()

        SectionLabel("Основное")

        FormField("Название", name, onValueChange = onNameChange)
        FormField("Бренд", brands, onValueChange = onBrandsChange)
        FormField("Состав", ingredients, singleLine = false, onValueChange = onIngredientsChange)

        HorizontalDivider()

        SectionLabel("КБЖУ на 100г")

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "КАЛОРИИ (ККАЛ)", color = TextMuted, fontSize = 11.sp)
                NumberField("", energyKcal, onEnergyKcalChange)

                Spacer(modifier = Modifier.height(4.dp))

                Text(text = "ЖИРЫ (Г)", color = TextMuted, fontSize = 11.sp)
                NumberField("", fat, onFatChange)

                Spacer(modifier = Modifier.height(4.dp))

                Text(text = "НАСЫЩ. ЖИРЫ(Г)", color = TextMuted, fontSize = 11.sp)
                NumberField("", saturatedFat, onSaturatedFatChange)

                Spacer(modifier = Modifier.height(4.dp))

                Text(text = "КЛЕТЧАТКА (Г)", color = TextMuted, fontSize = 11.sp)
                NumberField("", fiber, onFiberChange)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = "БЕЛКИ (г)", color = TextMuted, fontSize = 11.sp)
                NumberField("", proteins, onProteinsChange)

                Spacer(modifier = Modifier.height(4.dp))

                Text(text = "УГЛЕВОДЫ (Г)", color = TextMuted, fontSize = 11.sp)
                NumberField("", carbs, onCarbsChange)

                Spacer(modifier = Modifier.height(4.dp))

                Text(text = "САХАР (Г)", color = TextMuted, fontSize = 11.sp)
                NumberField("", sugars, onSugarsChange)

                Spacer(modifier = Modifier.height(4.dp))

                Text(text = "СОЛЬ (Г)", color = TextMuted, fontSize = 11.sp)
                NumberField("", salt, onSaltChange)
            }
        }

        HorizontalDivider()
        SectionLabel("Добавки E")
        Text("Через запятую: e102, e621, e330")
        FormField("Добавки", additives, onValueChange = onAdditivesChange)

        HorizontalDivider()
        Button(
            onClick = onSave,
            enabled = !isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentGreen,
                contentColor = AccentGreenDark
            )
        ) {
            if (isSaving) CircularProgressIndicator(modifier = Modifier.padding(end = 8.dp))
            Text("Сохранить изменения", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF111113)
@Composable
private fun EditFormPreview() {
    EditFormContent(
        name = "Молоко Простоквашино 3,2%",
        onNameChange = {},
        brands = "Простоквашино",
        onBrandsChange = {},
        ingredients = "Нормализованное молоко, витамин D3",
        onIngredientsChange = {},
        emoji = "🥛",
        onEmojiChange = {},
        energyKcal = "58",
        onEnergyKcalChange = {},
        proteins = "2.8",
        onProteinsChange = {},
        fat = "3.2",
        onFatChange = {},
        saturatedFat = "2.0",
        onSaturatedFatChange = {},
        carbs = "4.7",
        onCarbsChange = {},
        sugars = "4.7",
        onSugarsChange = {},
        fiber = "",
        onFiberChange = {},
        salt = "0.1",
        onSaltChange = {},
        additives = "e300",
        onAdditivesChange = {},
        isSaving = false,
        onSave = {},
        onOcrClick = {},
    )
}

@Composable
private fun SectionLabel(text: String) {
    Text(text, modifier = Modifier.padding(top = 4.dp))
}

@Composable
private fun FormField(
    label: String,
    value: String,
    singleLine: Boolean = true,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences
        ),
        minLines = if (singleLine) 1 else 3,
        modifier = modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = CardSurface,
            focusedContainerColor = CardSurface,
        ),
        shape = RoundedCornerShape(12.dp),
    )
}

@Composable
private fun NumberField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(text = "0") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = CardSurface,
            focusedContainerColor = CardSurface,
        ),
        shape = RoundedCornerShape(12.dp),
    )
}