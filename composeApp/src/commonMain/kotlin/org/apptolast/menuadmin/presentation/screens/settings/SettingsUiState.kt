package org.apptolast.menuadmin.presentation.screens.settings

import org.apptolast.menuadmin.domain.model.Restaurant

data class SettingsUiState(
    val isLoading: Boolean = true,
    val restaurant: Restaurant? = null,
    val isEditing: Boolean = false,
    val editName: String = "",
    val editDescription: String = "",
    val editAddress: String = "",
    val editPhone: String = "",
    val isSaving: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
)
