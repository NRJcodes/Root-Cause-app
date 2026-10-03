package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = EnterpriseAccent,
    onPrimary = Color.White,
    primaryContainer = EnterpriseSlateBlue,
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF38BDF8),
    onSecondary = Color.Black,
    background = Color(0xFF0B132B),
    surface = Color(0xFF132238),
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFF1E3A5F),
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = Color(0xFF334155)
)

private val LightColorScheme = lightColorScheme(
    primary = EnterpriseNavy,
    onPrimary = Color.White,
    primaryContainer = EnterpriseAccentSoft,
    onPrimaryContainer = EnterpriseNavyDark,
    secondary = EnterpriseAccent,
    onSecondary = Color.White,
    background = EnterpriseBackground,
    onBackground = EnterpriseCharcoal,
    surface = EnterpriseSurface,
    onSurface = EnterpriseCharcoal,
    surfaceVariant = EnterpriseSurfaceVariant,
    onSurfaceVariant = EnterpriseTextSecondary,
    outline = EnterpriseBorder,
    outlineVariant = EnterpriseBorderDarker
)

@Composable
fun RootCauseTheme(
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
