package com.example.chitaysostav.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ParseAdditivesUseCaseTest {

    private lateinit var useCase: ParseAdditivesUseCase

    @Before
    fun setup() {
        useCase = ParseAdditivesUseCase()
    }

    // ─── Базовые случаи ───────────────────────────────────────────────────────

    @Test
    fun `empty string returns empty list`() {
        val result = useCase("")
        assertTrue(result.isEmpty())
    }

    @Test
    fun `string without additives returns empty list`() {
        val result = useCase("вода, молоко, мука пшеничная, соль")
        assertTrue(result.isEmpty())
    }

    // ─── Поиск по E-номеру ────────────────────────────────────────────────────

    @Test
    fun `finds additive by uppercase E-number`() {
        val result = useCase("состав: вода, E102, сахар")
        assertEquals(1, result.size)
        assertEquals("e102", result[0].eNumber)
    }

    @Test
    fun `finds additive by lowercase e-number`() {
        val result = useCase("состав: вода, e102, сахар")
        assertEquals(1, result.size)
        assertEquals("e102", result[0].eNumber)
    }

    @Test
    fun `finds additive by cyrillic Е-number`() {
        // Кириллическая «Е» — отдельный символ в Unicode
        val result = useCase("состав: вода, Е102, сахар")
        assertEquals(1, result.size)
        assertEquals("e102", result[0].eNumber)
    }

    @Test
    fun `finds additive with letter suffix in E-number`() {
        // E150a — краситель «Сахарный колер I»
        val result = useCase("вода, E150a, карамель")
        assertEquals(1, result.size)
        assertEquals("e150a", result[0].eNumber)
    }

    @Test
    fun `unknown E-number returns empty list`() {
        val result = useCase("E999, E998")
        assertTrue(result.isEmpty())
    }

    @Test
    fun `finds multiple additives by E-numbers`() {
        val result = useCase("вода, E100, E102, E123")
        val eNumbers = result.map { it.eNumber }.toSet()
        assertEquals(3, result.size)
        assertTrue("e100" in eNumbers)
        assertTrue("e102" in eNumbers)
        assertTrue("e123" in eNumbers)
    }

    // ─── Поиск по алиасу ──────────────────────────────────────────────────────

    @Test
    fun `finds additive by Russian alias`() {
        val result = useCase("состав: вода, тартразин, соль")
        assertEquals(1, result.size)
        assertEquals("e102", result[0].eNumber)
    }

    @Test
    fun `finds additive by English alias`() {
        val result = useCase("water, tartrazine, salt")
        assertEquals(1, result.size)
        assertEquals("e102", result[0].eNumber)
    }

    @Test
    fun `alias search is case insensitive`() {
        val result = useCase("вода, ТАРТРАЗИН, соль")
        assertEquals(1, result.size)
        assertEquals("e102", result[0].eNumber)
    }

    @Test
    fun `finds additive by alias when alias is part of longer text`() {
        val result = useCase("краситель куркумин натуральный")
        assertEquals(1, result.size)
        assertEquals("e100", result[0].eNumber)
    }

    @Test
    fun `finds SAFE additive correctly`() {
        val result = useCase("куркумин")
        assertEquals(1, result.size)
        assertEquals("e100", result[0].eNumber)
    }

    @Test
    fun `finds DANGER additive correctly`() {
        // e123 — Амарант, DANGER
        val result = useCase("амарант")
        assertEquals(1, result.size)
        assertEquals("e123", result[0].eNumber)
    }

    // ─── Дедупликация ─────────────────────────────────────────────────────────

    @Test
    fun `deduplicates when same additive found by alias and E-number`() {
        // "куркумин" — алиас e100, "E100" — прямой E-номер
        val result = useCase("куркумин, E100")
        assertEquals(1, result.size)
        assertEquals("e100", result[0].eNumber)
    }

    @Test
    fun `deduplicates multiple matches of same additive`() {
        val result = useCase("тартразин, tartrazine, E102")
        assertEquals(1, result.size)
        assertEquals("e102", result[0].eNumber)
    }

    // ─── Смешанные сценарии ───────────────────────────────────────────────────

    @Test
    fun `finds additives from both alias and E-number in same string`() {
        // куркумин — алиас e100, E102 — прямой номер тартразина
        val result = useCase("куркумин, вода, E102")
        val eNumbers = result.map { it.eNumber }.toSet()
        assertEquals(2, result.size)
        assertTrue("e100" in eNumbers)
        assertTrue("e102" in eNumbers)
    }

    @Test
    fun `real ingredients string is parsed correctly`() {
        val ingredients = "вода питьевая, сахар, краситель тартразин (E102), куркумин (E100), соль"
        val result = useCase(ingredients)
        val eNumbers = result.map { it.eNumber }.toSet()
        // тартразин найдён и по алиасу и по E-номеру → 1 раз
        // куркумин найдён и по алиасу и по E-номеру → 1 раз
        assertEquals(2, result.size)
        assertTrue("e100" in eNumbers)
        assertTrue("e102" in eNumbers)
    }

    @Test
    fun `E-number without match in database is ignored`() {
        val result = useCase("E100, E999, E998")
        assertEquals(1, result.size)
        assertEquals("e100", result[0].eNumber)
    }
}
