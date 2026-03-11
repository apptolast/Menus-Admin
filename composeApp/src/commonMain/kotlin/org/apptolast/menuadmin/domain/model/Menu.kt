package org.apptolast.menuadmin.domain.model

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class Menu(
    val id: String,
    val name: String,
    val companyName: String,
    val dishes: List<Dish> = emptyList(),
    val isActive: Boolean = true,
    val createdAt: Instant,
    val updatedAt: Instant,
)

@Serializable
data class Dish(
    val id: String,
    val name: String,
    val description: String = "",
    val price: Double = 0.0,
    val category: DishCategory = DishCategory.ENTRANTE,
    val ingredients: List<String> = emptyList(),
    val allergens: Set<AllergenType> = emptySet(),
    val imageUrl: String? = null,
    val isAvailable: Boolean = true,
)

@Serializable
enum class DishCategory(
    val labelEs: String,
    val labelEn: String,
) {
    ENTRANTE("Entrante", "Starter"),
    GUARNICION("Guarnición", "Side"),
    ARROCES("Arroces", "Rice"),
    POSTRES("Postres", "Desserts"),
    COMBINADOS("Combinados", "Combos"),
    BEBIDAS("Bebidas", "Drinks"),
}
