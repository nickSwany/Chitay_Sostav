package com.example.chitaysostav.presentation.edit

sealed class EditProductUiState {
    object Idle : EditProductUiState()
    object Loading : EditProductUiState()
    object Saving : EditProductUiState()
    object Saved : EditProductUiState()
    data class Error(val message: String) : EditProductUiState()
}