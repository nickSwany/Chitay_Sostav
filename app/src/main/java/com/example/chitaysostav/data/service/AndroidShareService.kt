package com.example.chitaysostav.data.service

import android.content.Context
import android.content.Intent
import com.example.chitaysostav.domain.model.Product
import com.example.chitaysostav.domain.service.ShareService
import dagger.hilt.android.qualifiers.ApplicationContext

class AndroidShareService (
    @ApplicationContext private val context: Context
) : ShareService {

    override fun shareProduct(product: Product) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            val text = buildString {
                appendLine("${product.emoji} ${product.name}")
                if (product.brands.isNotEmpty()) appendLine("Бренд: ${product.brands}")
                if (product.ingredients.isNotEmpty()) appendLine(" \nСостав: ${product.ingredients}")
                appendLine("\nПроверено в приложении «Читай Состав»")
            }
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        context.startActivity(
            Intent.createChooser(sendIntent, "Поделится товаров")
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
} // добавить оценку для того чтобы она тоже отправлялась