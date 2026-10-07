package com.example.chitaysostav.domain.usecase.history

import com.example.chitaysostav.domain.model.ScannedProduct
import com.example.chitaysostav.domain.repository.HistoryRepository
import javax.inject.Inject

class SaveToHistoryUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
) {

    suspend operator fun invoke(product: ScannedProduct) {
        historyRepository.save(product)
    }

}