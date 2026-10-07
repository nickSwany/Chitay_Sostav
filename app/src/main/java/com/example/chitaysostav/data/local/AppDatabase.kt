package com.example.chitaysostav.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.chitaysostav.data.local.dao.ScannedProductDao
import com.example.chitaysostav.data.local.entity.ScannedProductEntity

@TypeConverters(Converters::class)
@Database(
    entities = [ScannedProductEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scannedProductDao(): ScannedProductDao

    companion object {
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE scanned_products ADD COLUMN addedByUser INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE scanned_products ADD COLUMN editedByUser INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}