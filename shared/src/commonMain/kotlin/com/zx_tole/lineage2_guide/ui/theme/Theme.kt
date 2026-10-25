package com.zx_tole.lineage2_guide.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = L2Primary,
    primaryContainer = L2PrimaryContainer,
    secondary = L2Secondary,
    tertiary = L2Tertiary,
    error = L2Error,
    background = L2DarkBackground,
    surface = L2DarkSurface,
    surfaceVariant = L2DarkSurfaceVariant,
    surfaceContainer = L2DarkCard,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onError = Color.Black,
    onBackground = Color(0xFFE0E0E0),
    onSurface = Color(0xFFE0E0E0),
    onSurfaceVariant = Color(0xFFB0B0C0),
    surfaceContainerHigh = L2DarkSurfaceVariant,
    surfaceContainerHighest = L2DarkSurfaceVariant
)

private val LightColorScheme = lightColorScheme(
    primary = L2LightPrimary,
    primaryContainer = L2LightPrimaryContainer,
    secondary = L2Secondary,
    tertiary = L2Tertiary,
    error = L2Error,
    background = L2LightBackground,
    surface = L2LightSurface,
    surfaceVariant = L2LightSurfaceVariant,
    surfaceContainer = L2LightCard,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.White,
    onError = Color.Black,
    onBackground = Color(0xFF1A1A1A),
    onSurface = Color(0xFF1A1A1A),
    onSurfaceVariant = Color(0xFF4A4A5A),
    surfaceContainerHigh = L2LightSurfaceVariant,
    surfaceContainerHighest = L2LightSurfaceVariant
)

@Composable
fun Lineage2Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
