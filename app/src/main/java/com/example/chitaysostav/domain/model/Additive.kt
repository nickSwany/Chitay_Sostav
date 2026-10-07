package com.example.chitaysostav.domain.model

data class Additive(
    val eNumber: String,
    val name: String,
    val risk: RiskLevel,
    val aliases: List<String>,
    val description: String = ""
)
