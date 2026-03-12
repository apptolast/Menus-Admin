package org.apptolast.menuadmin.data.repository

import org.apptolast.menuadmin.data.remote.auth.AuthResponseDto
import org.apptolast.menuadmin.data.remote.auth.AuthService
import org.apptolast.menuadmin.data.remote.auth.TokenManager
import org.apptolast.menuadmin.domain.repository.AuthRepository

class RemoteAuthRepository(
    private val authService: AuthService,
    private val tokenManager: TokenManager,
) : AuthRepository {
    override val isLoggedIn: Boolean get() = tokenManager.isLoggedIn

    override suspend fun login(
        email: String,
        password: String,
    ): AuthResponseDto {
        val response = authService.login(email, password)
        tokenManager.saveTokens(response.accessToken, response.refreshToken, response.expiresIn)
        return response
    }

    override suspend fun register(
        email: String,
        password: String,
        acceptTerms: Boolean,
    ): AuthResponseDto {
        val response = authService.register(email, password, acceptTerms)
        tokenManager.saveTokens(response.accessToken, response.refreshToken, response.expiresIn)
        return response
    }

    override suspend fun registerRestaurant(
        email: String,
        password: String,
        restaurantName: String,
        acceptTerms: Boolean,
    ): AuthResponseDto {
        val response = authService.registerRestaurant(email, password, restaurantName, acceptTerms)
        tokenManager.saveTokens(response.accessToken, response.refreshToken, response.expiresIn)
        return response
    }

    override suspend fun googleAuth(idToken: String): AuthResponseDto {
        val response = authService.googleCallback(idToken)
        tokenManager.saveTokens(response.accessToken, response.refreshToken, response.expiresIn)
        return response
    }

    override suspend fun grantConsent() {
        authService.grantConsent()
    }

    override suspend fun revokeConsent() {
        authService.revokeConsent()
    }

    override fun logout() {
        tokenManager.clearTokens()
    }
}
