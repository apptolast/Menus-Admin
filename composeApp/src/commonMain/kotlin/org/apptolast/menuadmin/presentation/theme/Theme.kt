package org.apptolast.menuadmin.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Blue500,
    onPrimary = TextWhite,
    primaryContainer = Blue100,
    onPrimaryContainer = Blue700,
    secondary = SidebarDark,
    onSecondary = TextWhite,
    secondaryContainer = SidebarDarkSurface,
    onSecondaryContainer = TextSidebar,
    background = BgSecondary,
    onBackground = TextPrimary,
    surface = BgPrimary,
    onSurface = TextPrimary,
    surfaceVariant = BgSecondary,
    onSurfaceVariant = TextSecondary,
    outline = BorderLight,
    outlineVariant = BorderMedium,
    error = Red500,
    onError = TextWhite,
    errorContainer = Red100,
    onErrorContainer = Red600,
)

@Composable
fun MenuAdminTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = appTypography(),
        content = content,
    )
}
