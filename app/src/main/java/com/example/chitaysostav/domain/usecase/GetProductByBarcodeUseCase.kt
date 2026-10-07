package com.example.chitaysostav.domain.usecase

import com.example.chitaysostav.domain.model.Product
import com.example.chitaysostav.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductByBarcodeUseCase @Inject constructor(private val repository: ProductRepository) {
    suspend operator fun invoke(barcode: String): Product? = repository.findByBarcode(barcode)
}