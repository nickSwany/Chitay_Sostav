package com.example.chitaysostav.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.chitaysostav.data.local.entity.ScannedProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScannedProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: ScannedProductEntity)

    @Query("SELECT * FROM scanned_products ORDER BY scannedAt DESC")
    fun getAll(): Flow<List<ScannedProductEntity>>

    @Query("SELECT * FROM scanned_products WHERE barcode = :barcode")
    suspend fun findByBarcode(barcode: String): ScannedProductEntity?

    @Query("DELETE FROM scanned_products")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM scanned_products")
    fun getScannedCount() : Flow<Int>

    @Query("SELECT COUNT(*) FROM scanned_products WHERE addedByUser = 1")
    fun getAddedByUser(): Flow<Int>

    @Query("SELECT COUNT(*) FROM scanned_products WHERE editedByUser = 1")
    fun getEditedByUser(): Flow<Int>
}