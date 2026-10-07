package com.example.chitaysostav.domain.model

enum class ScoreLevel(val label: String) {
    GOOD("Хороший"),
    AVERAGE("Умеренно"),
    BAD("Осторожно");

    companion object {
        fun from(score: Int): ScoreLevel = when {
            score >= 75 -> GOOD
            score >= 40 -> AVERAGE
            else -> BAD
        }
    }
}
