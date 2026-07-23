package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = AxonLime,
    onPrimary = Color(0xFF0A0A0A),
    primaryContainer = AthleticOrangeVariant,
    onPrimaryContainer = Color(0xFF0A0A0A),
    secondary = EmeraldGreen,
    onSecondary = Color(0xFF0A0A0A),
    tertiary = CyanAccent,
    onTertiary = Color(0xFF0A0A0A),
    background = DarkBackground,
    onBackground = TextPrimaryDark,
    surface = DarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondaryDark,
    outline = DarkCardBorder,
    error = FocusRed
)

@Composable
fun FitAiTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}

