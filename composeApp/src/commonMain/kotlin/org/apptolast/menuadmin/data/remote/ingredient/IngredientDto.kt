package org.apptolast.menuadmin.data.remote.ingredient

import kotlinx.serialization.Serializable

@Serializable
data class IngredientResponseDto(
    val id: String,
    val name: String,
    val brand: String = "",
    val supplier: String = "",
    val allergens: List<String> = emptyList(),
    val traces: List<String> = emptyList(),
    val ocrRawText: String? = null,
    val notes: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
)

@Serializable
data class IngredientRequestDto(
    val name: String,
    val brand: String? = null,
    val supplier: String? = null,
    val allergens: List<String>? = null,
    val traces: List<String>? = null,
    val ocrRawText: String? = null,
    val notes: String? = null,
)

@Serializable
data class AnalyzeTextRequestDto(
    val text: String,
)

@Serializable
data class AnalyzeTextResponseDto(
    val allergens: List<String> = emptyList(),
    val traces: List<String> = emptyList(),
)
