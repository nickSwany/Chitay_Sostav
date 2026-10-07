package com.example.chitaysostav.data.repository

import com.example.chitaysostav.data.local.dao.ScannedProductDao
import com.example.chitaysostav.data.local.entity.ScannedProductEntity
import com.example.chitaysostav.domain.model.Product
import com.example.chitaysostav.domain.repository.ProductRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ProductRepositoryImpl(
    private val dao: ScannedProductDao
) : ProductRepository {

    private val db = Firebase.firestore

    @Suppress("UNCHECKED_CAST")
    override suspend fun findByBarcode(barcode: String): Product? {
        dao.findByBarcode(barcode)?.let { entity ->
            return entity.toProduct()
        }
        return fetchFromFirestore(barcode)
    }

    override suspend fun save(barcode: String, product: Product) {
        saveToFirestore(barcode, product)
        val existing = dao.findByBarcode(barcode)
        if (existing != null) {
            syncRoomIfExists(barcode, product)
        } else {
            dao.insert(
                ScannedProductEntity(
                    barcode = barcode,
                    name = product.name,
                    brands = product.brands,
                    image = product.image,
                    emoji = product.emoji,
                    ingredients = product.ingredients,
                    additives = product.additives,
                    nutriments = product.nutriments,
                    addedByUser = true,
                    editedByUser = false,
                    scannedAt = System.currentTimeMillis()
                )
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    private suspend fun fetchFromFirestore(barcode: String): Product? =
        suspendCancellableCoroutine { cont ->
            db.collection("products").document(barcode).get()
                .addOnSuccessListener { doc ->
                    if (!doc.exists()) {
                        cont.resume(null)
                        return@addOnSuccessListener
                    }
                    cont.resume(
                        Product(
                            name = doc.getString("name") ?: "",
                            brands = doc.getString("brands") ?: "",
                            ingredients = doc.getString("ingredients") ?: "",
                            image = doc.getString("image") ?: "",
                            emoji = doc.getString("emoji") ?: "",
                            additives = doc.get("additives") as? List<String> ?: emptyList(),
                            nutriments = doc.get("nutriments") as? Map<String, Any> ?: emptyMap()
                        )
                    )
                }
                .addOnFailureListener { cont.resume(null) }
        }

    private suspend fun saveToFirestore(barcode: String, product: Product): Unit =
        suspendCancellableCoroutine { cont ->
            val data = mapOf(
                "name" to product.name,
                "brands" to product.brands,
                "ingredients" to product.ingredients,
                "image" to product.image,
                "emoji" to product.emoji,
                "additives" to product.additives,
                "nutriments" to product.nutriments
            )
            db.collection("products").document(barcode)
                .set(data)
                .addOnSuccessListener { cont.resume(Unit) }
                .addOnFailureListener { cont.resumeWithException(it) }
        }

    private suspend fun syncRoomIfExists(barcode: String, product: Product) {
        val existing = dao.findByBarcode(barcode) ?: return
        dao.insert(
            existing.copy(
                name = product.name,
                brands = product.brands,
                ingredients = product.ingredients,
                image = product.image,
                emoji = product.emoji,
                additives = product.additives,
                nutriments = product.nutriments,
                editedByUser = true
            )
        )
    }
}

private fun ScannedProductEntity.toProduct() = Product(
    name = name,
    brands = brands,
    ingredients = ingredients,
    image = image,
    emoji = emoji,
    additives = additives,
    nutriments = nutriments
)