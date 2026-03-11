package org.apptolast.menuadmin.data.remote.auth

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.apptolast.menuadmin.data.remote.ApiConstants

class AuthService(
    private val client: HttpClient,
) {
    suspend fun login(
        email: String,
        password: String,
    ): AuthResponseDto =
        client.post(ApiConstants.AUTH_LOGIN) {
            setBody(LoginRequestDto(email = email, password = password))
        }.body()

    suspend fun register(
        email: String,
        password: String,
        acceptTerms: Boolean = true,
    ): AuthResponseDto =
        client.post(ApiConstants.AUTH_REGISTER) {
            setBody(RegisterRequestDto(email = email, password = password, acceptTerms = acceptTerms))
        }.body()

    suspend fun refreshToken(refreshToken: String): AuthResponseDto =
        client.post(ApiConstants.AUTH_REFRESH) {
            setBody(RefreshTokenRequestDto(refreshToken = refreshToken))
        }.body()

    suspend fun googleCallback(idToken: String): AuthResponseDto =
        client.post(ApiConstants.AUTH_GOOGLE_CALLBACK) {
            setBody(GoogleCallbackRequestDto(idToken = idToken))
        }.body()

    suspend fun grantConsent() {
        client.post(ApiConstants.AUTH_CONSENT)
    }

    suspend fun revokeConsent() {
        client.delete(ApiConstants.AUTH_CONSENT)
    }
}
