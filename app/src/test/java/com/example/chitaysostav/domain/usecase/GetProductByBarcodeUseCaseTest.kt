package com.example.chitaysostav.domain.usecase

import com.example.chitaysostav.domain.model.Product
import com.example.chitaysostav.domain.repository.ProductRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class GetProductByBarcodeUseCaseTest {

    private lateinit var repository: ProductRepository
    private lateinit var useCase: GetProductByBarcodeUseCase

    private val testProduct = Product(
        name = "Молоко",
        brands = "Простоквашино",
        ingredients = "молоко пастеризованное",
        image = "",
        emoji = "🥛"
    )

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetProductByBarcodeUseCase(repository)
    }

    @Test
    fun `returns product when repository finds it`() = runTest {
        coEvery { repository.findByBarcode("4607038320123") } returns testProduct

        val result = useCase("4607038320123")

        assertEquals(testProduct, result)
    }

    @Test
    fun `returns null when repository returns null`() = runTest {
        coEvery { repository.findByBarcode(any()) } returns null

        val result = useCase("0000000000000")

        assertNull(result)
    }

    @Test
    fun `passes barcode to repository unchanged`() = runTest {
        val barcode = "4607038320123"
        coEvery { repository.findByBarcode(barcode) } returns testProduct

        useCase(barcode)

        coVerify(exactly = 1) { repository.findByBarcode(barcode) }
    }

    @Test
    fun `calls repository exactly once per invocation`() = runTest {
        coEvery { repository.findByBarcode(any()) } returns null

        useCase("1234567890")

        coVerify(exactly = 1) { repository.findByBarcode(any()) }
    }
}
