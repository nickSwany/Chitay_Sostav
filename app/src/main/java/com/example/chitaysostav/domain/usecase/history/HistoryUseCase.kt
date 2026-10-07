package com.example.chitaysostav.domain.usecase.history

import com.example.chitaysostav.domain.model.ScannedProduct
import com.example.chitaysostav.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HistoryUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
) {

    fun getAll(): Flow<List<ScannedProduct>> {
        return historyRepository.getAll()
    }

    suspend fun clearAll() {
        historyRepository.clearAll()
    }
}