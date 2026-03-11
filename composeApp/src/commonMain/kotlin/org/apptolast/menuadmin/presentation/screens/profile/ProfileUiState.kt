package org.apptolast.menuadmin.presentation.screens.profile

import org.apptolast.menuadmin.domain.model.AllergenType

data class ProfileUiState(
    val isLoading: Boolean = true,
    val hasConsent: Boolean = false,
    val allergenProfile: Set<AllergenType> = emptySet(),
    val severityNotes: String = "",
    val isEditing: Boolean = false,
    val editAllergens: Set<AllergenType> = emptySet(),
    val editSeverityNotes: String = "",
    val isSaving: Boolean = false,
    val showDeleteConfirm: Boolean = false,
    val showExportData: Boolean = false,
    val exportedData: String? = null,
    val error: String? = null,
    val successMessage: String? = null,
)
