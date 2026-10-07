package com.example.chitaysostav.domain.model

data class ProductScore(
    val score: Int,
    val nutriScore: Char,
    val nutriPoints: Int,
    val isReliable: Boolean
)