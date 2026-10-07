package com.example.chitaysostav.domain.model

data class Product(
    val name: String,
    val brands: String,
    val ingredients: String,
    val image: String,
    val emoji: String = "",
    val additives: List<String> = emptyList(),
    val nutriments: Map<String, Any> = emptyMap()
)