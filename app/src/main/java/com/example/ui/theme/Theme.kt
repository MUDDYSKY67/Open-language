package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PolyGreenPrimary,
    onPrimary = Slate900,
    primaryContainer = PolyGreenDark,
    onPrimaryContainer = PolyGreenLight,
    secondary = PolyBlueLight,
    onSecondary = Slate900,
    secondaryContainer = PolyBlueDark,
    onSecondaryContainer = PolyBlueLight,
    tertiary = PolyGoldTertiary,
    onTertiary = Slate900,
    tertiaryContainer = Slate800,
    onTertiaryContainer = PolyGoldLight,
    background = Slate900,
    onBackground = Slate50,
    surface = Slate800,
    onSurface = Slate50,
    surfaceVariant = Slate700,
    onSurfaceVariant = Slate300,
    error = PolyRubyHeart,
    onError = PureWhite
)

private val LightColorScheme = lightColorScheme(
    primary = PolyGreenDark,
    onPrimary = PureWhite,
    primaryContainer = Color(0xFFD1FAE5),
    onPrimaryContainer = Color(0xFF065F46),
    secondary = PolyBlueSecondary,
    onSecondary = PureWhite,
    secondaryContainer = Color(0xFFE0F2FE),
    onSecondaryContainer = PolyBlueDark,
    tertiary = PolyGoldTertiary,
    onTertiary = PureWhite,
    tertiaryContainer = Color(0xFFFEF3C7),
    onTertiaryContainer = Color(0xFF92400E),
    background = Slate50,
    onBackground = Slate900,
    surface = PureWhite,
    onSurface = Slate900,
    surfaceVariant = Slate100,
    onSurfaceVariant = Slate700,
    error = PolyRubyHeart,
    onError = PureWhite
)

@Composable
fun OpenLanguageTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent branding colors
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Keep alias for template compatibility
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) = OpenLanguageTheme(darkTheme, dynamicColor, content)
