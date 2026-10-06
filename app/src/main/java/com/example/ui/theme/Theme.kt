package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = SportsCyan,
    onPrimary = Color(0xFF041019),
    primaryContainer = Color(0xFF00363F),
    onPrimaryContainer = Color(0xFFBCEBFF),
    secondary = SportsEmerald,
    onSecondary = Color(0xFF00220E),
    secondaryContainer = Color(0xFF00391A),
    onSecondaryContainer = Color(0xFF86F8B0),
    tertiary = SportsGold,
    onTertiary = Color(0xFF261A00),
    tertiaryContainer = Color(0xFF4A3400),
    onTertiaryContainer = Color(0xFFFFDF9E),
    background = SportsBackgroundDark,
    onBackground = SportsTextPrimaryDark,
    surface = SportsSurfaceDark,
    onSurface = SportsTextPrimaryDark,
    surfaceVariant = SportsCardDark,
    onSurfaceVariant = SportsTextSecondaryDark,
    outline = SportsBorderDark,
    error = SportsCrimson,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF00687A),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBCEBFF),
    onPrimaryContainer = Color(0xFF001F26),
    secondary = Color(0xFF006D37),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF8AF8B4),
    onSecondaryContainer = Color(0xFF00210C),
    tertiary = Color(0xFF7B5800),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFDF9E),
    onTertiaryContainer = Color(0xFF261900),
    background = SportsBackgroundLight,
    onBackground = SportsTextPrimaryLight,
    surface = SportsSurfaceLight,
    onSurface = SportsTextPrimaryLight,
    surfaceVariant = SportsCardLight,
    onSurfaceVariant = SportsTextSecondaryLight,
    outline = SportsBorderLight,
    error = SportsCrimson,
    onError = Color.White
)

@Composable
fun SportsConnectTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
