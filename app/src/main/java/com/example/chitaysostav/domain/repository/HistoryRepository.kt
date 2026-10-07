package com.example.chitaysostav.domain.repository

import com.example.chitaysostav.domain.model.ProfileStats
import com.example.chitaysostav.domain.model.ScannedProduct
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    suspend fun save(product: ScannedProduct)
    fun getAll(): Flow<List<ScannedProduct>>
    suspend fun clearAll()
    fun getStats(): Flow<ProfileStats>
}