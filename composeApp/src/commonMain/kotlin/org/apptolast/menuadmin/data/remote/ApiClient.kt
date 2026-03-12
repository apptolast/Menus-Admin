package org.apptolast.menuadmin.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.apptolast.menuadmin.data.remote.auth.AuthResponseDto
import org.apptolast.menuadmin.data.remote.auth.RefreshTokenRequestDto
import org.apptolast.menuadmin.data.remote.auth.TokenManager

fun createHttpClient(
    tokenManager: TokenManager,
    json: Json,
): HttpClient {
    return HttpClient {
        install(ContentNegotiation) {
            json(json)
        }

        install(Logging) {
            level = LogLevel.HEADERS
        }

        install(Auth) {
            bearer {
                sendWithoutRequest { true }

                loadTokens {
                    val access = tokenManager.accessToken
                    val refresh = tokenManager.refreshToken
                    if (access != null && refresh != null) {
                        BearerTokens(access, refresh)
                    } else {
                        null
                    }
                }

                refreshTokens {
                    val refresh = tokenManager.refreshToken ?: return@refreshTokens null
                    try {
                        val response: AuthResponseDto = client.post(
                            "${ApiConstants.BASE_URL}${ApiConstants.AUTH_REFRESH}",
                        ) {
                            markAsRefreshTokenRequest()
                            contentType(ContentType.Application.Json)
                            setBody(RefreshTokenRequestDto(refreshToken = refresh))
                        }.body()
                        tokenManager.saveTokens(
                            response.accessToken,
                            response.refreshToken,
                            response.expiresIn,
                        )
                        BearerTokens(response.accessToken, response.refreshToken)
                    } catch (_: Exception) {
                        tokenManager.clearTokens()
                        null
                    }
                }
            }
        }

        defaultRequest {
            url(ApiConstants.BASE_URL)
            contentType(ContentType.Application.Json)
        }
    }
}
