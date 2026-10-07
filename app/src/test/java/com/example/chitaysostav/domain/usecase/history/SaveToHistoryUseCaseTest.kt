package com.example.chitaysostav.domain.usecase.history

import com.example.chitaysostav.domain.model.ScannedProduct
import com.example.chitaysostav.domain.repository.HistoryRepository
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SaveToHistoryUseCaseTest {

    private lateinit var repository: HistoryRepository
    private lateinit var useCase: SaveToHistoryUseCase

    private val testProduct = ScannedProduct(
        barcode = "4607038320123",
        name = "Молоко",
        brands = "Простоквашино",
        image = "",
        scannedAt = 1719000000000L
    )

    @Before
    fun setup() {
        repository = mockk()
        useCase = SaveToHistoryUseCase(repository)
    }

    @Test
    fun `delegates save to repository with correct product`() = runTest {
        coJustRun { repository.save(testProduct) }

        useCase(testProduct)

        coVerify(exactly = 1) { repository.save(testProduct) }
    }

    @Test
    fun `calls repository exactly once per invocation`() = runTest {
        coJustRun { repository.save(any()) }

        useCase(testProduct)

        coVerify(exactly = 1) { repository.save(any()) }
    }

    @Test
    fun `passes product unchanged to repository`() = runTest {
        val product = ScannedProduct(
            barcode = "0000000000001",
            name = "Кефир",
            brands = "Домик в деревне",
            image = "https://example.com/img.jpg",
            ingredients = "молоко, закваска",
            scannedAt = 1719111111111L
        )
        coJustRun { repository.save(product) }

        useCase(product)

        coVerify { repository.save(product) }
    }
}
