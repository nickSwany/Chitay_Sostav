package com.example.chitaysostav.presentation.history

import com.example.chitaysostav.domain.model.ScannedProduct

sealed class HistoryUiState {
    object Loading : HistoryUiState()
    data class Success(val products: List<ScannedProduct>) : HistoryUiState()
}