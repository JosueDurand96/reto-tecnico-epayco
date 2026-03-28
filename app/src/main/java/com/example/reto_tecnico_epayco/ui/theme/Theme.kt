package com.example.reto_tecnico_epayco.ui.theme

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

private val OnLight = Color(0xFFFFFFFF)

private val DarkColorScheme = darkColorScheme(
    primary = OceanBlue,
    onPrimary = OnLight,
    primaryContainer = OceanBlueDark,
    onPrimaryContainer = OnLight,
    secondary = Slate400,
    onSecondary = Slate950,
    surface = Slate950,
    onSurface = OnLight,
    surfaceContainer = Color(0xFF1E293B),
    surfaceContainerHigh = Color(0xFF334155),
    outline = OutlineSoft
)

private val LightColorScheme = lightColorScheme(
    primary = OceanBlue,
    onPrimary = OnLight,
    primaryContainer = Color(0xFFE3F2FD),
    onPrimaryContainer = OceanBlueDark,
    secondary = Slate600,
    onSecondary = OnLight,
    surface = SurfaceLight,
    onSurface = Slate950,
    surfaceContainer = Color(0xFFFFFFFF),
    surfaceContainerHigh = SurfaceMuted,
    outline = OutlineSoft
)

@Composable
fun CountriesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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
