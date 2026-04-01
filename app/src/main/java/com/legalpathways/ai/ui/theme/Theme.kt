package com.legalpathways.ai.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ── Deep Navy + Gold legal theme ─────────────────────────────────────────────
val NavyDeep       = Color(0xFF0A1628)
val NavyMid        = Color(0xFF1A2B4A)
val NavyLight      = Color(0xFF243B5E)
val GoldPrimary    = Color(0xFFD4A853)
val GoldLight      = Color(0xFFE8C37A)
val GoldDark       = Color(0xFFA8832E)
val CrimsonAccent  = Color(0xFFB33030)
val EmeraldAccent  = Color(0xFF2E7D52)
val SlateGray      = Color(0xFF8A9BB0)
val ParchmentLight = Color(0xFFF8F4ED)
val ParchmentMid   = Color(0xFFEFE8D8)
val SurfaceCard    = Color(0xFFFFFDF8)
val White          = Color(0xFFFFFFFF)
val DividerColor   = Color(0xFFE0D8C8)

val DarkColorScheme = darkColorScheme(
    primary          = GoldPrimary,
    onPrimary        = NavyDeep,
    primaryContainer = NavyLight,
    onPrimaryContainer = GoldLight,
    secondary        = SlateGray,
    onSecondary      = White,
    secondaryContainer = NavyMid,
    onSecondaryContainer = ParchmentLight,
    tertiary         = EmeraldAccent,
    onTertiary       = White,
    background       = NavyDeep,
    onBackground     = ParchmentLight,
    surface          = NavyMid,
    onSurface        = ParchmentLight,
    surfaceVariant   = NavyLight,
    onSurfaceVariant = SlateGray,
    error            = CrimsonAccent,
    outline          = Color(0xFF3A4F6B)
)

val LightColorScheme = lightColorScheme(
    primary          = NavyMid,
    onPrimary        = White,
    primaryContainer = ParchmentMid,
    onPrimaryContainer = NavyDeep,
    secondary        = GoldPrimary,
    onSecondary      = NavyDeep,
    secondaryContainer = Color(0xFFFFF3DC),
    onSecondaryContainer = GoldDark,
    tertiary         = EmeraldAccent,
    onTertiary       = White,
    background       = ParchmentLight,
    onBackground     = NavyDeep,
    surface          = SurfaceCard,
    onSurface        = NavyDeep,
    surfaceVariant   = ParchmentMid,
    onSurfaceVariant = NavyLight,
    error            = CrimsonAccent,
    outline          = DividerColor
)

@Composable
fun LegalPathwaysTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = LegalTypography,
        content     = content
    )
}