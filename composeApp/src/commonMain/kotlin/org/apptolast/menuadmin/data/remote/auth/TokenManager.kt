package org.apptolast.menuadmin.data.remote.auth

import com.russhwolf.settings.Settings

class TokenManager(
    private val settings: Settings = Settings(),
) {
    var accessToken: String?
        get() = settings.getStringOrNull(KEY_ACCESS_TOKEN)
        set(value) {
            if (value != null) {
                settings.putString(KEY_ACCESS_TOKEN, value)
            } else {
                settings.remove(KEY_ACCESS_TOKEN)
            }
        }

    var refreshToken: String?
        get() = settings.getStringOrNull(KEY_REFRESH_TOKEN)
        set(value) {
            if (value != null) {
                settings.putString(KEY_REFRESH_TOKEN, value)
            } else {
                settings.remove(KEY_REFRESH_TOKEN)
            }
        }

    var expiresIn: Long
        get() = settings.getLong(KEY_EXPIRES_IN, 0L)
        set(value) = settings.putLong(KEY_EXPIRES_IN, value)

    val isLoggedIn: Boolean get() = accessToken != null

    fun saveTokens(
        access: String,
        refresh: String,
        expires: Long,
    ) {
        accessToken = access
        refreshToken = refresh
        expiresIn = expires
    }

    fun clearTokens() {
        settings.remove(KEY_ACCESS_TOKEN)
        settings.remove(KEY_REFRESH_TOKEN)
        settings.remove(KEY_EXPIRES_IN)
    }

    private companion object {
        const val KEY_ACCESS_TOKEN = "access_token"
        const val KEY_REFRESH_TOKEN = "refresh_token"
        const val KEY_EXPIRES_IN = "expires_in"
    }
}
