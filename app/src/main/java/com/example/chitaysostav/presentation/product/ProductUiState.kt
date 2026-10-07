package com.example.chitaysostav.presentation.product

import com.example.chitaysostav.domain.model.Product
import com.example.chitaysostav.domain.model.Additive
import com.example.chitaysostav.domain.model.ProductScore

sealed class ProductUiState {
    object Loading : ProductUiState()
    data class Success(
        val product: Product,
        val additives: List<Additive>,
        val score: ProductScore?
    ) : ProductUiState()

    object NotFound : ProductUiState()
}