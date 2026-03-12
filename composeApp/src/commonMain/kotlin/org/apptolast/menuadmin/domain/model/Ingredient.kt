package org.apptolast.menuadmin.domain.model

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class Ingredient(
    val id: String,
    val name: String,
    val brand: String = "",
    val supplier: String = "",
    val allergens: Set<AllergenType> = emptySet(),
    val traces: Set<AllergenType> = emptySet(),
    val ocrRawText: String = "",
    val notes: String = "",
    val createdAt: Instant,
    val updatedAt: Instant,
)
