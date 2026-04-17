package com.inhohwangg.videodownloader.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary    = TossPrimary,
    onPrimary  = TossCardBg,
    background = TossBackground,
    onBackground = TossTextPrimary,
    surface    = TossCardBg,
    onSurface  = TossTextPrimary,
    secondary  = TossTextSecondary,
)

@Composable
fun VideoDownloaderTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography  = Typography,
        content     = content
    )
}
