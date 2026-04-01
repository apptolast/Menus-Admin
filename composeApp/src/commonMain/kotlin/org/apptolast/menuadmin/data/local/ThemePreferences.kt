package org.apptolast.menuadmin.data.local

import com.russhwolf.settings.Settings

class ThemePreferences(
    private val settings: Settings = Settings(),
) {
    var isDarkTheme: Boolean
        get() = settings.getBoolean(KEY_DARK_THEME, false)
        set(value) = settings.putBoolean(KEY_DARK_THEME, value)

    private companion object {
        const val KEY_DARK_THEME = "dark_theme"
    }
}
