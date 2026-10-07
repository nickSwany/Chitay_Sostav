package com.example.chitaysostav.domain.usecase

import com.example.chitaysostav.domain.model.Additive
import com.example.chitaysostav.domain.model.NutriData
import com.example.chitaysostav.domain.model.RiskLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CalculateProductScoreUseCaseTest {

    private lateinit var useCase: CalculateProductScoreUseCase

    @Before
    fun setup() {
        useCase = CalculateProductScoreUseCase()
    }

    // ─── Вспомогательные функции ──────────────────────────────────────────────

    private fun nutriData(
        kcal: Double = 0.0,
        sugars: Double = 0.0,
        saturatedFat: Double = 0.0,
        salt: Double = 0.0,
        proteins: Double = 0.0,
        fiber: Double = 0.0
    ) = NutriData(kcal, sugars, saturatedFat, salt, proteins, fiber)

    private fun additive(risk: RiskLevel) =
        Additive(eNumber = "E100", name = "Test", risk = risk, aliases = emptyList())

    // ─── Базовые случаи ───────────────────────────────────────────────────────

    @Test
    fun `returns null when nutriData is null`() {
        val result = useCase(nutriData = null, additives = emptyList(), ingredients = "")
        assertNull(result)
    }

    @Test
    fun `returns non-null when nutriData is present`() {
        val result = useCase(nutriData(), emptyList(), "")
        assertNotNull(result)
    }

    // ─── Вода и нулевые значения ──────────────────────────────────────────────

    @Test
    fun `water with all zeros gets score 100`() {
        val result = useCase(nutriData(), emptyList(), "")!!
        assertEquals(100, result.score)
    }

    @Test
    fun `water gets nutriScore B`() {
        // rawScore = 0 - 0 = 0, что соответствует 'B'
        val result = useCase(nutriData(), emptyList(), "")!!
        assertEquals('B', result.nutriScore)
    }

    // ─── NutriScore буква ─────────────────────────────────────────────────────

    @Test
    fun `nutriScore A when rawScore is negative`() {
        // proteins=1.7 → proteinPoints=1, rawScore = 0 - 1 = -1
        val result = useCase(nutriData(proteins = 1.7), emptyList(), "")!!
        assertEquals('A', result.nutriScore)
    }

    @Test
    fun `nutriScore B when rawScore is 0`() {
        val result = useCase(nutriData(), emptyList(), "")!!
        assertEquals('B', result.nutriScore)
    }

    @Test
    fun `nutriScore B when rawScore is 2`() {
        // kcal=670 → 1pt, sugars=4.6 → 1pt. rawScore = 2
        val result = useCase(nutriData(kcal = 670.0, sugars = 4.6), emptyList(), "")!!
        assertEquals('B', result.nutriScore)
    }

    @Test
    fun `nutriScore C when rawScore is 3`() {
        // kcal=1005 → 2pt, sugars=4.6 → 1pt. rawScore = 3
        val result = useCase(nutriData(kcal = 1005.0, sugars = 4.6), emptyList(), "")!!
        assertEquals('C', result.nutriScore)
    }

    @Test
    fun `nutriScore C when rawScore is 10`() {
        // kcal=2010 → 5pt, sugars=9.1 → 2pt, saturatedFat=3.1 → 3pt. rawScore = 10
        val result = useCase(nutriData(kcal = 2010.0, sugars = 9.1, saturatedFat = 3.1), emptyList(), "")!!
        assertEquals('C', result.nutriScore)
    }

    @Test
    fun `nutriScore D when rawScore is 11`() {
        // kcal=2010 → 5pt, sugars=9.1 → 2pt, saturatedFat=3.1 → 3pt, salt=0.21 → 1pt. rawScore = 11
        val result = useCase(nutriData(kcal = 2010.0, sugars = 9.1, saturatedFat = 3.1, salt = 0.21), emptyList(), "")!!
        assertEquals('D', result.nutriScore)
    }

    @Test
    fun `nutriScore D when rawScore is 18`() {
        // kcal=3015 → 8pt, sugars=9.1 → 2pt, saturatedFat=6.1 → 6pt, salt=0.41 → 2pt. rawScore = 18
        val result = useCase(nutriData(kcal = 3015.0, sugars = 9.1, saturatedFat = 6.1, salt = 0.41), emptyList(), "")!!
        assertEquals('D', result.nutriScore)
    }

    @Test
    fun `nutriScore E when rawScore is 19 or more`() {
        // kcal=3015 → 8pt, sugars=9.1 → 2pt, saturatedFat=6.1 → 6pt, salt=0.61 → 3pt. rawScore = 19
        val result = useCase(nutriData(kcal = 3015.0, sugars = 9.1, saturatedFat = 6.1, salt = 0.61), emptyList(), "")!!
        assertEquals('E', result.nutriScore)
    }

    // ─── Реальные примеры ─────────────────────────────────────────────────────

    @Test
    fun `chicken breast gets nutriScore A`() {
        // 165ккал, 0 сахар, 1г насыщ. жир, 0.07г соль, 31г белок, 0 клетчатка
        val data = nutriData(kcal = 165.0, saturatedFat = 1.0, salt = 0.07, proteins = 31.0)
        val result = useCase(data, emptyList(), "")!!
        assertEquals('A', result.nutriScore)
    }

    @Test
    fun `heavily processed food gets nutriScore E`() {
        // Высококалорийный продукт с сахаром и жирами
        val data = nutriData(kcal = 500.0, sugars = 60.0, saturatedFat = 11.0, salt = 2.1)
        val result = useCase(data, emptyList(), "")!!
        assertEquals('E', result.nutriScore)
    }

    // ─── Добавки ──────────────────────────────────────────────────────────────

    @Test
    fun `no additives - no penalty, score is 100 for water`() {
        val result = useCase(nutriData(), emptyList(), "")!!
        assertEquals(100, result.score)
    }

    @Test
    fun `one SAFE additive - no penalty`() {
        val result = useCase(nutriData(), listOf(additive(RiskLevel.SAFE)), "")!!
        assertEquals(100, result.score)
    }

    @Test
    fun `one WARN additive - 5 points penalty`() {
        // nutriComponent = 60, additivePenalty = 5, score = 60 + (40 - 5) = 95
        val result = useCase(nutriData(), listOf(additive(RiskLevel.WARN)), "")!!
        assertEquals(95, result.score)
    }

    @Test
    fun `one DANGER additive - 15 points penalty`() {
        // nutriComponent = 60, additivePenalty = 15, score = 60 + (40 - 15) = 85
        val result = useCase(nutriData(), listOf(additive(RiskLevel.DANGER)), "")!!
        assertEquals(85, result.score)
    }

    @Test
    fun `additive penalty is capped at 40`() {
        // 3 × DANGER = 45 → capped at 40 → score = 60 + (40 - 40) = 60
        val additives = List(3) { additive(RiskLevel.DANGER) }
        val result = useCase(nutriData(), additives, "")!!
        assertEquals(60, result.score)
    }

    @Test
    fun `multiple WARN additives accumulate penalty`() {
        // 4 × WARN = 20 penalty → score = 60 + (40 - 20) = 80
        val additives = List(4) { additive(RiskLevel.WARN) }
        val result = useCase(nutriData(), additives, "")!!
        assertEquals(80, result.score)
    }

    @Test
    fun `mixed additives calculate correctly`() {
        // DANGER(15) + WARN(5) = 20 → score = 60 + (40 - 20) = 80
        val additives = listOf(additive(RiskLevel.DANGER), additive(RiskLevel.WARN))
        val result = useCase(nutriData(), additives, "")!!
        assertEquals(80, result.score)
    }

    // ─── isReliable ───────────────────────────────────────────────────────────

    @Test
    fun `isReliable is true when no suspicious ingredients`() {
        val result = useCase(nutriData(), emptyList(), "вода, молоко, мука")!!
        assertTrue(result.isReliable)
    }

    @Test
    fun `isReliable is false when sugar in Russian and sugars value is zero`() {
        val result = useCase(nutriData(sugars = 0.0), emptyList(), "вода, сахар, ваниль")!!
        assertFalse(result.isReliable)
    }

    @Test
    fun `isReliable is false when sugar in English and sugars value is zero`() {
        val result = useCase(nutriData(sugars = 0.0), emptyList(), "water, sugar, vanilla")!!
        assertFalse(result.isReliable)
    }

    @Test
    fun `isReliable is false when salt in Russian and salt value is zero`() {
        val result = useCase(nutriData(salt = 0.0), emptyList(), "мука, соль, вода")!!
        assertFalse(result.isReliable)
    }

    @Test
    fun `isReliable is false when salt in English and salt value is zero`() {
        val result = useCase(nutriData(salt = 0.0), emptyList(), "flour, salt, water")!!
        assertFalse(result.isReliable)
    }

    @Test
    fun `isReliable is true when sugar in ingredients and sugars value is non-zero`() {
        val result = useCase(nutriData(sugars = 5.0), emptyList(), "вода, сахар, ваниль")!!
        assertTrue(result.isReliable)
    }

    @Test
    fun `isReliable is true when salt in ingredients and salt value is non-zero`() {
        val result = useCase(nutriData(salt = 1.0), emptyList(), "мука, соль, вода")!!
        assertTrue(result.isReliable)
    }

    @Test
    fun `isReliable is false when either sugar or salt is suspicious`() {
        // соль в составе, соль = 0 → ненадёжно, даже если сахар ок
        val result = useCase(nutriData(sugars = 5.0, salt = 0.0), emptyList(), "сахар, соль")!!
        assertFalse(result.isReliable)
    }

    @Test
    fun `isReliable check is case insensitive via lowercase`() {
        val result = useCase(nutriData(sugars = 0.0), emptyList(), "Вода, САХАР, Ваниль")!!
        assertFalse(result.isReliable)
    }

    // ─── Граничные значения energyPoints ──────────────────────────────────────

    @Test
    fun `energyPoints 0 at boundary 335 kcal`() {
        // rawScore = 0 → nutriScore B
        val result = useCase(nutriData(kcal = 335.0), emptyList(), "")!!
        assertEquals('B', result.nutriScore)
    }

    @Test
    fun `energyPoints 1 just above boundary 335 kcal`() {
        val below = useCase(nutriData(kcal = 335.0), emptyList(), "")!!
        val above = useCase(nutriData(kcal = 335.1), emptyList(), "")!!
        assertEquals(1, above.nutriPoints - below.nutriPoints)
    }

    @Test
    fun `energyPoints max 10 above 3350 kcal`() {
        // 3350 → 9 (последняя ступень перед максимумом), 3350.1 → 10 (максимум)
        val justAbove = useCase(nutriData(kcal = 3350.1), emptyList(), "")!!
        val wayAbove = useCase(nutriData(kcal = 9999.0), emptyList(), "")!!
        assertEquals(justAbove.nutriPoints, wayAbove.nutriPoints)
    }

    // ─── Граничные значения sugarPoints ───────────────────────────────────────

    @Test
    fun `sugarPoints 0 at boundary 4_5g`() {
        val at = useCase(nutriData(sugars = 4.5), emptyList(), "")!!
        val zero = useCase(nutriData(), emptyList(), "")!!
        assertEquals(zero.nutriPoints, at.nutriPoints)
    }

    @Test
    fun `sugarPoints increases just above 4_5g`() {
        val at = useCase(nutriData(sugars = 4.5), emptyList(), "")!!
        val above = useCase(nutriData(sugars = 4.6), emptyList(), "")!!
        assertEquals(1, above.nutriPoints - at.nutriPoints)
    }

    @Test
    fun `sugarPoints max 10 above 45g`() {
        // 45.0 → 9 (последняя ступень перед максимумом), 45.1 → 10 (максимум)
        val justAbove = useCase(nutriData(sugars = 45.1), emptyList(), "")!!
        val wayAbove = useCase(nutriData(sugars = 100.0), emptyList(), "")!!
        assertEquals(justAbove.nutriPoints, wayAbove.nutriPoints)
    }

    // ─── Граничные значения saltPoints ────────────────────────────────────────

    @Test
    fun `saltPoints 0 at boundary 0_2g`() {
        val at = useCase(nutriData(salt = 0.2), emptyList(), "")!!
        val zero = useCase(nutriData(), emptyList(), "")!!
        assertEquals(zero.nutriPoints, at.nutriPoints)
    }

    @Test
    fun `saltPoints increases just above 0_2g`() {
        val at = useCase(nutriData(salt = 0.2), emptyList(), "")!!
        val above = useCase(nutriData(salt = 0.21), emptyList(), "")!!
        assertEquals(1, above.nutriPoints - at.nutriPoints)
    }

    // ─── Граничные значения proteinPoints ─────────────────────────────────────

    @Test
    fun `proteinPoints 0 at boundary 1_6g`() {
        val at = useCase(nutriData(proteins = 1.6), emptyList(), "")!!
        val zero = useCase(nutriData(), emptyList(), "")!!
        assertEquals(zero.nutriPoints, at.nutriPoints)
    }

    @Test
    fun `proteinPoints max 5 above 8g`() {
        // 8.0 → 4 очка (последняя ступень), 8.1 → 5 очков (максимум)
        val justAbove = useCase(nutriData(proteins = 8.1), emptyList(), "")!!
        val wayAbove = useCase(nutriData(proteins = 100.0), emptyList(), "")!!
        assertEquals(justAbove.nutriPoints, wayAbove.nutriPoints)
    }

    // ─── nutriComponent coerceIn ──────────────────────────────────────────────

    @Test
    fun `nutriComponent capped at 60 for very healthy product`() {
        // proteins=8 → 4pt positive, rawScore = 0 - 4 = -4 → nutriComponent = (44/40)*60 = 66 → capped at 60
        // finalScore = 60 + 40 = 100
        val result = useCase(nutriData(proteins = 8.0), emptyList(), "")!!
        assertEquals(100, result.score)
    }

    @Test
    fun `finalScore is always between 0 and 100`() {
        // Worst case: max rawScore = 40 (all negative max, no positives)
        // nutriComponent = 0, additivePenalty = 40 (capped), score = 0 + 0 = 0
        val worstData = nutriData(
            kcal = 9999.0, sugars = 100.0, saturatedFat = 100.0, salt = 100.0
        )
        val worstAdditives = List(3) { additive(RiskLevel.DANGER) }
        val worst = useCase(worstData, worstAdditives, "")!!
        assertTrue(worst.score >= 0)

        // Best case: water, no additives
        val best = useCase(nutriData(), emptyList(), "")!!
        assertTrue(best.score <= 100)
    }

    // ─── nutriPoints в результате ─────────────────────────────────────────────

    @Test
    fun `nutriPoints equals rawScore in result`() {
        // kcal=1005 → 2pt, sugars=4.6 → 1pt. rawScore = 3
        val result = useCase(nutriData(kcal = 1005.0, sugars = 4.6), emptyList(), "")!!
        assertEquals(3, result.nutriPoints)
    }

    @Test
    fun `nutriPoints is negative for high-protein low-calorie product`() {
        val result = useCase(nutriData(proteins = 8.0), emptyList(), "")!!
        assertTrue(result.nutriPoints < 0)
    }
}
