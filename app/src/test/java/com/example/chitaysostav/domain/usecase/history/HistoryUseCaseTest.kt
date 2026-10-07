package com.example.chitaysostav.domain.usecase.history

import com.example.chitaysostav.domain.model.ScannedProduct
import com.example.chitaysostav.domain.repository.HistoryRepository
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class HistoryUseCaseTest {

    private lateinit var repository: HistoryRepository
    private lateinit var useCase: HistoryUseCase

    private val product1 = ScannedProduct(
        barcode = "111", name = "Молоко", brands = "A", image = "", scannedAt = 1000L
    )
    private val product2 = ScannedProduct(
        barcode = "222", name = "Кефир", brands = "B", image = "", scannedAt = 2000L
    )

    @Before
    fun setup() {
        repository = mockk()
        useCase = HistoryUseCase(repository)
    }

    // ─── getAll ───────────────────────────────────────────────────────────────

    @Test
    fun `getAll returns flow from repository`() = runTest {
        val expected = listOf(product1, product2)
        every { repository.getAll() } returns flowOf(expected)

        val result = useCase.getAll().first()

        assertEquals(expected, result)
    }

    @Test
    fun `getAll delegates to repository exactly once`() = runTest {
        every { repository.getAll() } returns flowOf(emptyList())

        useCase.getAll()

        verify(exactly = 1) { repository.getAll() }
    }

    @Test
    fun `getAll returns empty flow when repository is empty`() = runTest {
        every { repository.getAll() } returns flowOf(emptyList())

        val result = useCase.getAll().first()

        assertEquals(emptyList<ScannedProduct>(), result)
    }

    @Test
    fun `getAll preserves order from repository`() = runTest {
        val ordered = listOf(product2, product1) // product2 новее
        every { repository.getAll() } returns flowOf(ordered)

        val result = useCase.getAll().first()

        assertEquals(product2, result[0])
        assertEquals(product1, result[1])
    }

    // ─── clearAll ─────────────────────────────────────────────────────────────

    @Test
    fun `clearAll delegates to repository`() = runTest {
        coJustRun { repository.clearAll() }

        useCase.clearAll()

        coVerify(exactly = 1) { repository.clearAll() }
    }

    @Test
    fun `clearAll calls repository exactly once`() = runTest {
        coJustRun { repository.clearAll() }

        useCase.clearAll()

        coVerify(exactly = 1) { repository.clearAll() }
    }
}
