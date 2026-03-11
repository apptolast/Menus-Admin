package org.apptolast.menuadmin.presentation.screens.cartadigital

import org.apptolast.menuadmin.domain.model.AllergenType
import org.apptolast.menuadmin.domain.model.Dish
import org.apptolast.menuadmin.domain.model.Menu

data class CartaDigitalUiState(
    val isLoading: Boolean = true,
    val availableMenus: List<Menu> = emptyList(),
    val selectedMenuId: String? = null,
    val selectedAllergens: Set<AllergenType> = emptySet(),
    val allDishes: List<Dish> = emptyList(),
    val safeDishes: List<Dish> = emptyList(),
    val unsafeDishes: List<Dish> = emptyList(),
    val error: String? = null,
)
