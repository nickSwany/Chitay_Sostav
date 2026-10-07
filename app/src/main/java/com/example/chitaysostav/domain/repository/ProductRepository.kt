package com.example.chitaysostav.domain.repository

import com.example.chitaysostav.domain.model.Product

interface ProductRepository {
    suspend fun findByBarcode(barcode: String): Product?
    suspend fun save(barcode: String, product: Product)
}