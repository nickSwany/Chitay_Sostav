package com.example.chitaysostav.data.repository

import com.example.chitaysostav.data.local.dao.ScannedProductDao
import com.example.chitaysostav.data.local.entity.ScannedProductEntity
import com.example.chitaysostav.domain.model.ProfileStats
import com.example.chitaysostav.domain.model.ScannedProduct
import com.example.chitaysostav.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class HistoryRepositoryImpl(
    private val dao: ScannedProductDao
) : HistoryRepository {

    override suspend fun save(product: ScannedProduct) {
        dao.insert(product.toEntity())
    }

    override fun getAll(): Flow<List<ScannedProduct>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    override suspend fun clearAll() {
        dao.deleteAll()
    }

    override fun getStats(): Flow<ProfileStats> = combine(
        dao.getScannedCount(),
        dao.getAddedByUser(),
        dao.getEditedByUser()
    ) { scanned, added, edited ->
        ProfileStats(scanned = scanned, added = added, edited = edited)
    }
}

private fun ScannedProduct.toEntity() = ScannedProductEntity(
    barcode = barcode,
    name = name,
    brands = brands,
    image = image,
    emoji = emoji,
    ingredients = ingredients,
    additives = additives,
    nutriments = nutriments,
    scannedAt = scannedAt,
    addedByUser = addedByUser,
    editedByUser = editedByUser
)

private fun ScannedProductEntity.toDomain() = ScannedProduct(
    barcode = barcode,
    name = name,
    brands = brands,
    image = image,
    emoji = emoji,
    ingredients = ingredients,
    additives = additives,
    nutriments = nutriments,
    scannedAt = scannedAt,
    addedByUser = addedByUser,
    editedByUser = editedByUser
)