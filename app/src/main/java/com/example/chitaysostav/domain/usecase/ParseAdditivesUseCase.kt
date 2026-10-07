package com.example.chitaysostav.domain.usecase

import com.example.chitaysostav.data.local.AdditiveDatabase
import com.example.chitaysostav.domain.model.Additive
import javax.inject.Inject

class ParseAdditivesUseCase @Inject constructor() {

    private val eNumberRegex = Regex("[EeЕе](\\d{3,4}[a-zA-Z]?)", RegexOption.IGNORE_CASE)

    operator fun invoke(ingredients: String): List<Additive> {
        val byAliases = AdditiveDatabase.findByText(ingredients)
        val byENumbers = eNumberRegex.findAll(ingredients)
            .mapNotNull { match ->
                val eNumber = "e${match.groupValues[1].lowercase()}"
                AdditiveDatabase.findByENumber(eNumber)
            }
            .toList()
        return (byAliases + byENumbers).distinctBy { it.eNumber }
    }

}