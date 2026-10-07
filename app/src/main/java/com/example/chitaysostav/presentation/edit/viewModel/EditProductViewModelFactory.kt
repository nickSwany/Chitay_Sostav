package com.example.chitaysostav.presentation.edit.viewModel

import dagger.assisted.AssistedFactory

@AssistedFactory
interface EditProductViewModelFactory {
    fun create(barcode: String): EditProductViewModel
}
