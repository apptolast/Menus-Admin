package org.apptolast.menuadmin.domain.repository

import org.apptolast.menuadmin.data.remote.auth.AuthResponseDto

interface AuthRepository {
    val isLoggedIn: Boolean

    suspend fun login(
        email: String,
        password: String,
    ): AuthResponseDto

    suspend fun register(
        email: String,
        password: String,
        acceptTerms: Boolean = true,
    ): AuthResponseDto

    suspend fun googleAuth(idToken: String): AuthResponseDto

    suspend fun grantConsent()

    suspend fun revokeConsent()

    fun logout()
}
