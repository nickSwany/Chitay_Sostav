package com.example.chitaysostav.domain.usecase

import com.example.chitaysostav.domain.model.Product
import com.example.chitaysostav.domain.repository.ProductRepository
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SaveProductUseCaseTest {

    private lateinit var repository: ProductRepository
    private lateinit var useCase: SaveProductUseCase

    private val testBarcode = "4607038320123"
    private val testProduct = Product(
        name = "Кефир",
        brands = "Домик в деревне",
        ingredients = "молоко, закваска",
        image = "",
        emoji = "🥛"
    )

    @Before
    fun setup() {
        repository = mockk()
        useCase = SaveProductUseCase(repository)
    }

    @Test
    fun `delegates save to repository with correct barcode and product`() = runTest {
        coJustRun { repository.save(testBarcode, testProduct) }

        useCase(testBarcode, testProduct)

        coVerify(exactly = 1) { repository.save(testBarcode, testProduct) }
    }

    @Test
    fun `calls repository exactly once`() = runTest {
        coJustRun { repository.save(any(), any()) }

        useCase(testBarcode, testProduct)

        coVerify(exactly = 1) { repository.save(any(), any()) }
    }

    @Test
    fun `passes barcode and product unchanged to repository`() = runTest {
        val barcode = "9999999999999"
        val product = Product(
            name = "Тест",
            brands = "Бренд",
            ingredients = "состав",
            image = "url"
        )
        coJustRun { repository.save(barcode, product) }

        useCase(barcode, product)

        coVerify { repository.save(barcode, product) }
    }
}
