package com.example.chitaysostav.presentation.product

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitaysostav.data.local.AdditiveDatabase
import com.example.chitaysostav.domain.model.ScannedProduct
import com.example.chitaysostav.domain.model.Additive
import com.example.chitaysostav.domain.model.NutriData
import com.example.chitaysostav.domain.model.Product
import com.example.chitaysostav.domain.usecase.CalculateProductScoreUseCase
import com.example.chitaysostav.domain.usecase.GetProductByBarcodeUseCase
import com.example.chitaysostav.domain.usecase.ParseAdditivesUseCase
import com.example.chitaysostav.domain.usecase.ShareProductUseCase
import com.example.chitaysostav.domain.usecase.history.SaveToHistoryUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ProductViewModel.Factory::class)
class ProductViewModel @AssistedInject constructor(
    @Assisted val barcode: String,
    private val getProductByBarcode: GetProductByBarcodeUseCase,
    private val parseAdditivesUseCase: ParseAdditivesUseCase,
    private val saveProductToHistory: SaveToHistoryUseCase,
    private val calculateProductScore: CalculateProductScoreUseCase,
    private val shareProductUseCase: ShareProductUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Loading)
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    init {
        Log.d("ProductViewModel", "init called, barcode=$barcode")
        fetchProduct()
    }

    private fun fetchProduct() {
        viewModelScope.launch {
            val product = getProductByBarcode(barcode)
            if (product != null) {
                val additives = parseAdditives(product.ingredients, product.additives)
                val nutriData = product.nutriments.let { n ->
                    val kcal = (n["energy-kcal_100g"] as? Number)?.toDouble() ?: return@let null
                    NutriData(
                        kcal = kcal,
                        sugars = (n["sugars_100g"] as? Number)?.toDouble() ?: 0.0,
                        saturatedFat = (n["saturated-fat_100g"] as? Number)?.toDouble() ?: 0.0,
                        salt = (n["salt_100g"] as? Number)?.toDouble() ?: 0.0,
                        proteins = (n["proteins_100g"] as? Number)?.toDouble() ?: 0.0,
                        fiber = (n["fiber_100g"] as? Number)?.toDouble() ?: 0.0,
                    )
                }
                val scoreProduct = calculateProductScore(nutriData, additives, product.ingredients)
                _uiState.value = ProductUiState.Success(product, additives, scoreProduct)
                try {
                    Log.d("ProductViewModel", "saving to history: ${product.name}")
                    saveProductToHistory(
                        ScannedProduct(
                            barcode = barcode,
                            name = product.name,
                            brands = product.brands,
                            image = product.image,
                            emoji = product.emoji,
                            ingredients = product.ingredients,
                            additives = product.additives,
                            nutriments = product.nutriments,
                        )
                    )
                } catch (e: Exception) {
                    Log.e("ProductViewmodel", "saveToHistory failed", e)
                }
            } else {
                _uiState.value = ProductUiState.NotFound
            }
        }
    }

    private fun parseAdditives(ingredients: String, additives: List<String>): List<Additive> {
        val additivesFromIngredients = parseAdditivesUseCase(ingredients)
        val additivesFromDatabase = additives.mapNotNull { AdditiveDatabase.findByENumber(it) }
        val result = additivesFromIngredients + additivesFromDatabase
        return result.distinctBy { it.eNumber }
    }

    @AssistedFactory
    interface Factory {
        fun create(barcode: String): ProductViewModel
    }

    fun shareProduct(product: Product) {
        shareProductUseCase(product)
    }
}