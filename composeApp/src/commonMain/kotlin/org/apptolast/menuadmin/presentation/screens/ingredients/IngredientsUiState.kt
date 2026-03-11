package org.apptolast.menuadmin.presentation.screens.ingredients

import org.apptolast.menuadmin.domain.model.AllergenType
import org.apptolast.menuadmin.domain.model.Ingredient

data class IngredientsUiState(
    val isLoading: Boolean = true,
    val ingredients: List<Ingredient> = emptyList(),
    val searchQuery: String = "",
    val filterAllergens: Set<AllergenType> = emptySet(),
    val isEditing: Boolean = false,
    val editingIngredient: Ingredient? = null,
    // Form fields
    val formName: String = "",
    val formBrand: String = "",
    val formDescription: String = "",
    val formLabelInfo: String = "",
    val formAllergens: Set<AllergenType> = emptySet(),
    val error: String? = null,
)
