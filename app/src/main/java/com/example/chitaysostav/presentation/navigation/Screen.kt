package com.example.chitaysostav.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object History : Screen("history")
    object Profile : Screen("profile")
    object Camera : Screen("camera")
    object Product : Screen("product/{barcode}/{timestamp}") {
        fun route(barcode: String) = "product/$barcode/${System.currentTimeMillis()}"
    }

    object Edit : Screen("edit/{barcode}/{timestamp}") {
        fun route(barcode: String) = "edit/$barcode/${System.currentTimeMillis()}"
    }
    object PrivacyPolicy : Screen("privacy-policy")
}