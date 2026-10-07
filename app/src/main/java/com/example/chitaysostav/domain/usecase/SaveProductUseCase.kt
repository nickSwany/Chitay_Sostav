package com.example.chitaysostav.domain.usecase

import com.example.chitaysostav.domain.model.Product
import com.example.chitaysostav.domain.repository.ProductRepository
import javax.inject.Inject

class SaveProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(barcode: String, product: Product) =
        productRepository.save(barcode, product)

}