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

private val DarkColorScheme =
  darkColorScheme(
    primary = BhakariGold,
    onPrimary = Color.Black,
    primaryContainer = BhakariNavyCard,
    onPrimaryContainer = BhakariGoldLight,
    secondary = BhakariCrimson,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF4C0519),
    onSecondaryContainer = Color(0xFFFFD1DC),
    tertiary = VerifiedGreen,
    onTertiary = Color.White,
    background = DarkBackground,
    onBackground = TextPrimaryDark,
    surface = DarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondaryDark,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = BhakariGoldDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFEF3C7),
    onPrimaryContainer = Color(0xFF78350F),
    secondary = BhakariCrimson,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFE4E6),
    onSecondaryContainer = Color(0xFF881337),
    tertiary = VerifiedGreenDark,
    onTertiary = Color.White,
    background = LightBackground,
    onBackground = TextPrimaryLight,
    surface = LightSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = TextSecondaryLight,
  )

@Composable
fun BhakariTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep Bhakari vibrant signature styling
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

// Keep alias for compatibility
@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) = BhakariTheme(darkTheme, dynamicColor, content)
