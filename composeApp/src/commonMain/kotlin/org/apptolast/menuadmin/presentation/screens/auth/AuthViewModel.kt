package org.apptolast.menuadmin.presentation.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.apptolast.menuadmin.domain.repository.AuthRepository

class AuthViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState(isAuthenticated = authRepository.isLoggedIn))
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, error = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, error = null) }
    }

    fun onAcceptTermsChange(accept: Boolean) {
        _uiState.update { it.copy(acceptTerms = accept) }
    }

    fun onToggleMode() {
        _uiState.update { it.copy(isLoginMode = !it.isLoginMode, error = null) }
    }

    fun onLogin() {
        val state = _uiState.value
        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(error = "Introduce email y contraseña") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                authRepository.login(state.email.trim(), state.password)
                _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al iniciar sesión: ${e.message ?: "Error desconocido"}",
                    )
                }
            }
        }
    }

    fun onRegister() {
        val state = _uiState.value
        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(error = "Introduce email y contraseña") }
            return
        }
        if (state.password.length < 8) {
            _uiState.update { it.copy(error = "La contraseña debe tener al menos 8 caracteres") }
            return
        }
        if (!state.acceptTerms) {
            _uiState.update { it.copy(error = "Debes aceptar los términos") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                authRepository.register(state.email.trim(), state.password, state.acceptTerms)
                _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al registrarse: ${e.message ?: "Error desconocido"}",
                    )
                }
            }
        }
    }

    fun onGoogleAuth(idToken: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                authRepository.googleAuth(idToken)
                _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error con Google: ${e.message ?: "Error desconocido"}",
                    )
                }
            }
        }
    }

    fun onLogout() {
        authRepository.logout()
        _uiState.update {
            AuthUiState(isAuthenticated = false)
        }
    }

    fun onDismissError() {
        _uiState.update { it.copy(error = null) }
    }
}
