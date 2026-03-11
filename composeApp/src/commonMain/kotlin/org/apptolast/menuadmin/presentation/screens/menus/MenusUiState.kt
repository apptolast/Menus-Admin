package org.apptolast.menuadmin.presentation.screens.menus

import org.apptolast.menuadmin.domain.model.DishCategory
import org.apptolast.menuadmin.domain.model.Menu

data class MenusUiState(
    val isLoading: Boolean = true,
    val menus: List<Menu> = emptyList(),
    val selectedMenu: Menu? = null,
    val selectedCategory: DishCategory? = null,
    val searchQuery: String = "",
    val error: String? = null,
)
