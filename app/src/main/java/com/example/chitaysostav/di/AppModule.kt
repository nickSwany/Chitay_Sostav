package com.example.chitaysostav.di

import android.content.Context
import androidx.room.Room
import com.example.chitaysostav.data.local.AppDatabase
import com.example.chitaysostav.data.local.dao.ScannedProductDao
import com.example.chitaysostav.data.repository.HistoryRepositoryImpl
import com.example.chitaysostav.data.repository.ProductRepositoryImpl
import com.example.chitaysostav.data.service.AndroidShareService
import com.example.chitaysostav.domain.repository.HistoryRepository
import com.example.chitaysostav.domain.repository.ProductRepository
import com.example.chitaysostav.domain.service.ShareService
import com.example.chitaysostav.domain.usecase.CalculateProductScoreUseCase
import com.example.chitaysostav.domain.usecase.history.HistoryUseCase
import com.example.chitaysostav.domain.usecase.GetProductByBarcodeUseCase
import com.example.chitaysostav.domain.usecase.SaveProductUseCase
import com.example.chitaysostav.domain.usecase.ShareProductUseCase
import com.example.chitaysostav.domain.usecase.history.SaveToHistoryUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "chitay_sostav.db")
            .addMigrations(AppDatabase.MIGRATION_2_3)
            .build()

    @Provides
    @Singleton
    fun provideScannedProductDao(db: AppDatabase): ScannedProductDao =
        db.scannedProductDao()

    @Provides
    @Singleton
    fun provideProductRepository(dao: ScannedProductDao): ProductRepository =
        ProductRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideHistoryRepository(dao: ScannedProductDao): HistoryRepository =
        HistoryRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideShareService(@ApplicationContext context: Context): ShareService =
        AndroidShareService(context)
}