package org.apptolast.menuadmin.data.remote.allergen

import kotlinx.serialization.Serializable

@Serializable
data class AllergenResponseDto(
    val id: Int,
    val code: String,
    val iconUrl: String = "",
    val translations: Map<String, String> = emptyMap(),
)
