package com.example.chitaysostav.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scanned_products")
data class ScannedProductEntity(
    @PrimaryKey
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