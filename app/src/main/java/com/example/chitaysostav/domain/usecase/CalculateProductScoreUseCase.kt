package com.example.chitaysostav.domain.usecase

import com.example.chitaysostav.domain.model.Additive
import com.example.chitaysostav.domain.model.NutriData
import com.example.chitaysostav.domain.model.ProductScore
import com.example.chitaysostav.domain.model.RiskLevel
import javax.inject.Inject

class CalculateProductScoreUseCase @Inject constructor() {

    operator fun invoke(
        nutriData: NutriData?,
        additives: List<Additive>,
        ingredients: String
    ): ProductScore? {

        if (nutriData == null) return null

        val negative =
            energyPoints(nutriData.kcal) + sugarPoints(nutriData.sugars) + saturatedFatPoints(
                nutriData.saturatedFat
            ) + saltPoints(nutriData.salt)

        val positive =
            proteinPoints(nutriData.proteins) + fiberPoints(nutriData.fiber)

        val rawScore = negative - positive

        val nutriScoreLetter = when {
            rawScore <= -1 -> 'A'
            rawScore <= 2 -> 'B'
            rawScore <= 10 -> 'C'
            rawScore <= 18 -> 'D'
            else -> 'E'
        }

        val nutriComponent = ((40 - rawScore) / 40.0 * 60).coerceIn(0.0, 60.0)

        val additivePenalty = additives.sumOf {
            when (it.risk) {
                RiskLevel.SAFE -> 0
                RiskLevel.WARN -> 5
                RiskLevel.DANGER -> 15
            }
        }.coerceAtMost(40)

        val finalScore = (nutriComponent + (40 - additivePenalty)).toInt()

        val lower = ingredients.lowercase()
        val isReliable = !(
                (lower.contains("сахар") || lower.contains("sugar"))
                        && nutriData.sugars == 0.0 ||
                        (lower.contains("соль") || lower.contains("salt"))
                        && nutriData.salt == 0.0
                )

        return ProductScore(
            score = finalScore,
            nutriScore = nutriScoreLetter,
            nutriPoints = rawScore,
            isReliable = isReliable
        )
    }

    private fun energyPoints(kcal: Double) = when {
        kcal <= 335 -> 0
        kcal <= 670 -> 1
        kcal <= 1005 -> 2
        kcal <= 1340 -> 3
        kcal <= 1675 -> 4
        kcal <= 2010 -> 5
        kcal <= 2345 -> 6
        kcal <= 2680 -> 7
        kcal <= 3015 -> 8
        kcal <= 3350 -> 9
        else -> 10
    }

    private fun sugarPoints(sugar: Double) = when {
        sugar <= 4.5 -> 0
        sugar <= 9 -> 1
        sugar <= 13.5 -> 2
        sugar <= 18 -> 3
        sugar <= 22.5 -> 4
        sugar <= 27 -> 5
        sugar <= 31 -> 6
        sugar <= 36 -> 7
        sugar <= 40 -> 8
        sugar <= 45 -> 9
        else -> 10
    }

    private fun saturatedFatPoints(saturatedFat: Double) = when {
        saturatedFat <= 1 -> 0
        saturatedFat <= 2 -> 1
        saturatedFat <= 3 -> 2
        saturatedFat <= 4 -> 3
        saturatedFat <= 5 -> 4
        saturatedFat <= 6 -> 5
        saturatedFat <= 7 -> 6
        saturatedFat <= 8 -> 7
        saturatedFat <= 9 -> 8
        saturatedFat <= 10 -> 9
        else -> 10
    }

    private fun saltPoints(salt: Double) = when {
        salt <= 0.2 -> 0
        salt <= 0.4 -> 1
        salt <= 0.6 -> 2
        salt <= 0.8 -> 3
        salt <= 1.0 -> 4
        salt <= 1.2 -> 5
        salt <= 1.4 -> 6
        salt <= 1.6 -> 7
        salt <= 1.8 -> 8
        salt <= 2.0 -> 9
        else -> 10
    }

    private fun proteinPoints(protein: Double) = when {
        protein <= 1.6 -> 0
        protein <= 3.2 -> 1
        protein <= 4.8 -> 2
        protein <= 6.4 -> 3
        protein <= 8.0 -> 4
        else -> 5
    }

    private fun fiberPoints(fiber: Double) = when {
        fiber <= 0.9 -> 0
        fiber <= 1.9 -> 1
        fiber <= 2.8 -> 2
        fiber <= 3.7 -> 3
        fiber <= 4.7 -> 4
        else -> 5
    }
}