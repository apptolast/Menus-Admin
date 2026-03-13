package org.apptolast.menuadmin.domain.repository

import org.apptolast.menuadmin.data.remote.auth.AuthResponseDto

interface AuthRepository {
    val isLoggedIn: Boolean

    suspend fun login(
        email: String,
        password: String,
    ): AuthResponseDto

    suspend fun registerAdmin(
        email: String,
        password: String,
        name: String? = null,
    ): AuthResponseDto

    fun logout()
}
