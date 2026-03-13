package org.apptolast.menuadmin.presentation.screens.recipes

import org.apptolast.menuadmin.domain.model.DishCategory
import org.apptolast.menuadmin.domain.model.Ingredient
import org.apptolast.menuadmin.domain.model.Recipe
import org.apptolast.menuadmin.domain.model.RecipeIngredient

data class RecipesUiState(
    val isLoading: Boolean = true,
    val recipes: List<Recipe> = emptyList(),
    val allIngredients: List<Ingredient> = emptyList(),
    val searchQuery: String = "",
    val isEditing: Boolean = false,
    val editingRecipe: Recipe? = null,
    val formName: String = "",
    val formDescription: String = "",
    val formCategory: DishCategory = DishCategory.ENTRANTE,
    val formIngredients: List<RecipeIngredient> = emptyList(),
    val formIsActive: Boolean = true,
    val isSaving: Boolean = false,
    val error: String? = null,
)
