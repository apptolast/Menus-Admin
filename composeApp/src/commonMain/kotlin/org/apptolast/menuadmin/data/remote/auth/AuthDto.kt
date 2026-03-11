package org.apptolast.menuadmin.data.remote.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    val email: String,
    val password: String,
)

@Serializable
data class RegisterRequestDto(
    val email: String,
    val password: String,
    val acceptTerms: Boolean = true,
)

@Serializable
data class RefreshTokenRequestDto(
    val refreshToken: String,
)

@Serializable
data class GoogleCallbackRequestDto(
    val idToken: String,
)

@Serializable
data class AuthResponseDto(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String = "Bearer",
    val expiresIn: Long = 900_000,
)
