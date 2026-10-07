package com.example.chitaysostav.domain.usecase

import com.example.chitaysostav.domain.model.Product
import com.example.chitaysostav.domain.service.ShareService
import javax.inject.Inject

class ShareProductUseCase @Inject constructor(
    private val shareService: ShareService
) {
    operator fun invoke(product: Product) {
        shareService.shareProduct(product)
    }
}