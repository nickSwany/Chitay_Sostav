package com.example.chitaysostav.presentation.edit.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitaysostav.domain.model.Product
import com.example.chitaysostav.domain.usecase.GetProductByBarcodeUseCase
import com.example.chitaysostav.domain.usecase.SaveProductUseCase
import com.example.chitaysostav.presentation.edit.EditProductUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = EditProductViewModelFactory::class)
class EditProductViewModel @AssistedInject constructor(
    @Assisted val barcode: String,
    private val getProductByBarcode: GetProductByBarcodeUseCase,
    private val saveProduct: SaveProductUseCase
) : ViewModel() {

    val name = MutableStateFlow("")
    val brands = MutableStateFlow("")
    val ingredients = MutableStateFlow("")
    val emoji = MutableStateFlow("")
    val energyKcal = MutableStateFlow("")
    val proteins = MutableStateFlow("")
    val fat = MutableStateFlow("")
    val saturatedFat = MutableStateFlow("")
    val carbs = MutableStateFlow("")
    val sugars = MutableStateFlow("")
    val fiber = MutableStateFlow("")
    val salt = MutableStateFlow("")
    val additives = MutableStateFlow("")

    private var originalName = ""
    private var originalBrands = ""
    private var originalIngredients = ""
    private var originalEmoji = ""
    private var originalEnergyKcal = ""
    private var originalProteins = ""
    private var originalFat = ""
    private var originalSaturatedFat = ""
    private var originalCarbs = ""
    private var originalSugars = ""
    private var originalFiber = ""
    private var originalSalt = ""
    private var originalAdditives = ""


    private val _uiState = MutableStateFlow<EditProductUiState>(EditProductUiState.Loading)
    val uiState: StateFlow<EditProductUiState> = _uiState.asStateFlow()

    init {
        loadProduct()
    }

    private fun loadProduct() {
        viewModelScope.launch {
            val product = getProductByBarcode(barcode)
            if (product != null) prefill(product)
            _uiState.value = EditProductUiState.Idle
        }
    }

    private fun prefill(product: Product) {
        name.value = product.name
        brands.value = product.brands
        ingredients.value = product.ingredients
        emoji.value = product.emoji
        energyKcal.value = product.nutriments["energy-kcal_100g"]?.toString() ?: ""
        proteins.value = product.nutriments["proteins_100g"]?.toString() ?: ""
        fat.value = product.nutriments["fat_100g"]?.toString() ?: ""
        saturatedFat.value = product.nutriments["saturated-fat_100g"]?.toString() ?: ""
        carbs.value = product.nutriments["carbohydrates_100g"]?.toString() ?: ""
        sugars.value = product.nutriments["sugars_100g"]?.toString() ?: ""
        fiber.value = product.nutriments["fiber_100g"]?.toString() ?: ""
        salt.value = product.nutriments["salt_100g"]?.toString() ?: ""
        additives.value = product.additives.joinToString(", ")

        originalName = product.name
        originalBrands = product.brands
        originalIngredients = product.ingredients
        originalEmoji = product.emoji
        originalEnergyKcal = product.nutriments["energy-kcal_100g"]?.toString() ?: ""
        originalProteins = product.nutriments["proteins_100g"]?.toString() ?: ""
        originalFat = product.nutriments["fat_100g"]?.toString() ?: ""
        originalSaturatedFat = product.nutriments["saturated-fat_100g"]?.toString() ?: ""
        originalCarbs = product.nutriments["carbohydrates_100g"]?.toString() ?: ""
        originalSugars = product.nutriments["sugars_100g"]?.toString() ?: ""
        originalFiber = product.nutriments["fiber_100g"]?.toString() ?: ""
        originalSalt = product.nutriments["salt_100g"]?.toString() ?: ""
        originalAdditives = product.additives.joinToString(", ")
    }

    fun save() {
        viewModelScope.launch {
            _uiState.value = EditProductUiState.Saving
            try {
                val nutriments = buildMap<String, Any> {
                    energyKcal.value.toDoubleOrNull()?.let { put("energy-kcal_100g", it) }
                    proteins.value.toDoubleOrNull()?.let { put("proteins_100g", it) }
                    fat.value.toDoubleOrNull()?.let { put("fat_100g", it) }
                    saturatedFat.value.toDoubleOrNull()?.let { put("saturated-fat_100g", it) }
                    carbs.value.toDoubleOrNull()?.let { put("carbohydrates_100g", it) }
                    sugars.value.toDoubleOrNull()?.let { put("sugars_100g", it) }
                    fiber.value.toDoubleOrNull()?.let { put("fiber_100g", it) }
                    salt.value.toDoubleOrNull()?.let { put("salt_100g", it) }
                }
                val additivesList = additives.value
                    .split(",")
                    .map { it.trim().lowercase() }
                    .filter { it.isNotEmpty() }

                saveProduct(
                    barcode, Product(
                        name = name.value.trim(),
                        brands = brands.value.trim(),
                        ingredients = ingredients.value.trim(),
                        image = "",
                        emoji = emoji.value,
                        additives = additivesList,
                        nutriments = nutriments
                    )
                )
                _uiState.value = EditProductUiState.Saved
            } catch (e: Exception) {
                _uiState.value =
                    EditProductUiState.Error(e.message ?: "Error saving product")
            }
        }
    }

    fun hasChanges(): Boolean {
        return name.value != originalName
                || brands.value != originalBrands
                || ingredients.value != originalIngredients
                || emoji.value != originalEmoji
                || additives.value != originalAdditives
                || energyKcal.value != originalEnergyKcal
                || proteins.value != originalProteins
                || fat.value != originalFat
                || saturatedFat.value != originalSaturatedFat
                || carbs.value != originalCarbs
                || sugars.value != originalSugars
                || fiber.value != originalFiber
                || salt.value != originalSalt
    }

}