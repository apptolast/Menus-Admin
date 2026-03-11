package org.apptolast.menuadmin.domain.model

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class Ingredient(
    val id: String,
    val name: String,
    val description: String = "",
    val brand: String = "",
    val allergens: Set<AllergenType> = emptySet(),
    val labelInfo: String = "",
    val createdAt: Instant,
    val updatedAt: Instant,
)
