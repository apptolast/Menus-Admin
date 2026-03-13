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

    override suspend fun registerAdmin(
        email: String,
        password: String,
        name: String?,
    ): AuthResponseDto {
        val response = authService.registerAdmin(email, password, name)
        tokenManager.saveTokens(response.accessToken, response.refreshToken, response.expiresIn)
        return response
    }

    override fun logout() {
        tokenManager.clearTokens()
    }
}
