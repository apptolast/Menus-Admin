package org.apptolast.menuadmin.domain.model

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class Recipe(
    val id: String,
    val name: String,
    val description: String = "",
    val ingredients: List<RecipeIngredient> = emptyList(),
    val subRecipeIds: List<String> = emptyList(),
    val isActive: Boolean = true,
    val category: DishCategory = DishCategory.ENTRANTE,
    val createdAt: Instant,
    val updatedAt: Instant,
)

@Serializable
data class RecipeIngredient(
    val ingredientId: String,
    val ingredientName: String,
    val quantity: Double,
    val unit: String,
)
