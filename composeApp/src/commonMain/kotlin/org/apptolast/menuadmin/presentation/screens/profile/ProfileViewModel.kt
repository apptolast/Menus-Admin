package org.apptolast.menuadmin.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.apptolast.menuadmin.data.remote.auth.AuthService
import org.apptolast.menuadmin.data.remote.consumer.AllergenProfileRequestDto
import org.apptolast.menuadmin.data.remote.consumer.ConsumerService
import org.apptolast.menuadmin.domain.model.AllergenType

class ProfileViewModel(
    private val consumerService: ConsumerService,
    private val authService: AuthService,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val profile = consumerService.getAllergenProfile()
                val allergens = profile.allergenCodes.mapNotNull { AllergenType.fromApiCode(it) }.toSet()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        hasConsent = true,
                        allergenProfile = allergens,
                        severityNotes = profile.severityNotes,
                    )
                }
            } catch (e: Exception) {
                // If 404/403, user may not have consent or profile yet
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        hasConsent = false,
                    )
                }
            }
        }
    }

    fun grantConsent() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            try {
                authService.grantConsent()
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        hasConsent = true,
                        successMessage = "Consentimiento otorgado",
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = "Error al otorgar consentimiento: ${e.message ?: "Error desconocido"}",
                    )
                }
            }
        }
    }

    fun revokeConsent() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            try {
                authService.revokeConsent()
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        hasConsent = false,
                        allergenProfile = emptySet(),
                        severityNotes = "",
                        successMessage = "Consentimiento revocado",
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = "Error al revocar consentimiento: ${e.message ?: "Error desconocido"}",
                    )
                }
            }
        }
    }

    fun startEditing() {
        _uiState.update {
            it.copy(
                isEditing = true,
                editAllergens = it.allergenProfile,
                editSeverityNotes = it.severityNotes,
            )
        }
    }

    fun cancelEditing() {
        _uiState.update { it.copy(isEditing = false) }
    }

    fun toggleAllergen(allergen: AllergenType) {
        _uiState.update { state ->
            val current = state.editAllergens
            state.copy(
                editAllergens = if (allergen in current) current - allergen else current + allergen,
            )
        }
    }

    fun onSeverityNotesChange(notes: String) {
        _uiState.update { it.copy(editSeverityNotes = notes) }
    }

    fun saveProfile() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            try {
                val request = AllergenProfileRequestDto(
                    allergenCodes = state.editAllergens.map { it.apiCode },
                    severityNotes = state.editSeverityNotes,
                )
                consumerService.updateAllergenProfile(request)
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        isEditing = false,
                        allergenProfile = state.editAllergens,
                        severityNotes = state.editSeverityNotes,
                        successMessage = "Perfil actualizado",
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = "Error al guardar: ${e.message ?: "Error desconocido"}",
                    )
                }
            }
        }
    }

    fun exportData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val data = consumerService.exportData()
                val text = buildString {
                    appendLine("Usuario: ${data.email}")
                    appendLine("Rol: ${data.role}")
                    data.allergenProfile?.let { profile ->
                        appendLine("Alergenos: ${profile.allergenCodes.joinToString(", ")}")
                        if (profile.severityNotes.isNotEmpty()) {
                            appendLine("Notas: ${profile.severityNotes}")
                        }
                    }
                    appendLine("Exportado: ${data.exportedAt ?: "ahora"}")
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        showExportData = true,
                        exportedData = text,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al exportar datos: ${e.message ?: "Error desconocido"}",
                    )
                }
            }
        }
    }

    fun dismissExportData() {
        _uiState.update { it.copy(showExportData = false, exportedData = null) }
    }

    fun showDeleteConfirm() {
        _uiState.update { it.copy(showDeleteConfirm = true) }
    }

    fun dismissDeleteConfirm() {
        _uiState.update { it.copy(showDeleteConfirm = false) }
    }

    fun deleteAccount(onDeleted: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null, showDeleteConfirm = false) }
            try {
                consumerService.deleteAccount()
                onDeleted()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = "Error al eliminar cuenta: ${e.message ?: "Error desconocido"}",
                    )
                }
            }
        }
    }

    fun dismissMessage() {
        _uiState.update { it.copy(error = null, successMessage = null) }
    }
}
