package com.example.chitaysostav.domain.model

data class  ScannedProduct(
    val barcode: String,
    val name: String,
    val brands: String,
    val image: String,
    val emoji: String = "",
    val ingredients: String = "",
    val additives: List<String> = emptyList(),
    val nutriments: Map<String, Any> = emptyMap(),
    val scannedAt: Long = System.currentTimeMillis(),
    val addedByUser: Boolean = false,
    val editedByUser: Boolean = false,
)