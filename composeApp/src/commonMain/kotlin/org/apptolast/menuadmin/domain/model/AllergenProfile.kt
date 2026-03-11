package org.apptolast.menuadmin.domain.model

data class AllergenProfile(
    val profileUuid: String = "",
    val allergenCodes: List<String> = emptyList(),
    val severityNotes: String = "",
)
