package com.example.chitaysostav.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AppColorScheme = darkColorScheme(
    primary              = AccentGreen,
    onPrimary            = AccentGreenDark,
    primaryContainer     = AccentGreenDim,
    onPrimaryContainer   = AccentGreen,
    secondary            = AccentGreen,
    onSecondary          = AccentGreenDark,
    secondaryContainer   = AccentGreenDim,   // фон индикатора активной вкладки
    onSecondaryContainer = AccentGreen,      // иконка/текст активной вкладки
    background           = Background,
    onBackground         = TextPrimary,
    surface              = CardSurface,
    onSurface            = TextPrimary,
    surfaceVariant       = SurfaceVariant,
    onSurfaceVariant     = TextMuted,        // иконка/текст неактивной вкладки
    outline              = Border,
    outlineVariant       = Border,
)

@Composable
fun ChitaySostavTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography  = Typography,
        content     = content
    )
}
