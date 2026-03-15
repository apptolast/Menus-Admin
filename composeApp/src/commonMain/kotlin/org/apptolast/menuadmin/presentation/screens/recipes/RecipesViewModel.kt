package org.apptolast.menuadmin.presentation.screens.recipes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.apptolast.menuadmin.domain.model.Recipe
import org.apptolast.menuadmin.domain.model.RecipeIngredient
import org.apptolast.menuadmin.domain.repository.IngredientRepository
import org.apptolast.menuadmin.domain.repository.RecipeRepository

class RecipesViewModel(
    private val recipeRepository: RecipeRepository,
    private val ingredientRepository: IngredientRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val restaurantId: String = savedStateHandle["restaurantId"] ?: ""

    private val _formState = MutableStateFlow(RecipesUiState())

    val uiState: StateFlow<RecipesUiState> = combine(
        recipeRepository.getRecipesByRestaurant(restaurantId),
        ingredientRepository.getAllIngredients(),
        _formState,
    ) { recipes, ingredients, formState ->
        val filtered = if (formState.searchQuery.isBlank()) {
            recipes
        } else {
            recipes.filter { recipe ->
                recipe.name.contains(formState.searchQuery, ignoreCase = true) ||
                    recipe.description.contains(formState.searchQuery, ignoreCase = true)
            }
        }
        val categories = recipes
            .map { it.category }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
        formState.copy(
            isLoading = false,
            recipes = filtered,
            allIngredients = ingredients,
            availableCategories = categories,
        )
    }
        .catch { throwable ->
            emit(
                _formState.value.copy(
                    isLoading = false,
                    error = throwable.message ?: "Error al cargar recetas",
                ),
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = RecipesUiState(),
        )

    fun onSearchQueryChange(query: String) {
        _formState.value = _formState.value.copy(searchQuery = query)
    }

    fun onNewRecipe() {
        _formState.value = _formState.value.copy(
            isEditing = true,
            editingRecipe = null,
            formName = "",
            formDescription = "",
            formCategory = "",
            formIngredients = emptyList(),
            formIsActive = true,
        )
    }

    fun onEditRecipe(recipe: Recipe) {
        viewModelScope.launch {
            val fullRecipe = recipeRepository.getRecipeById(recipe.id) ?: recipe
            _formState.value = _formState.value.copy(
                isEditing = true,
                editingRecipe = fullRecipe,
                formName = fullRecipe.name,
                formDescription = fullRecipe.description,
                formCategory = fullRecipe.category,
                formIngredients = fullRecipe.ingredients,
                formIsActive = fullRecipe.isActive,
            )
        }
    }

    fun onDismissEditor() {
        _formState.value = _formState.value.copy(
            isEditing = false,
            isSaving = false,
            editingRecipe = null,
            formName = "",
            formDescription = "",
            formCategory = "",
            formIngredients = emptyList(),
            formIsActive = true,
        )
    }

    fun onFormNameChange(value: String) {
        _formState.value = _formState.value.copy(formName = value)
    }

    fun onFormDescriptionChange(value: String) {
        _formState.value = _formState.value.copy(formDescription = value)
    }

    fun onFormCategoryChange(category: String) {
        _formState.value = _formState.value.copy(formCategory = category)
    }

    fun onToggleActive() {
        _formState.value = _formState.value.copy(formIsActive = !_formState.value.formIsActive)
    }

    fun onAddIngredientToForm(ingredient: RecipeIngredient) {
        val current = _formState.value.formIngredients
        _formState.value = _formState.value.copy(formIngredients = current + ingredient)
    }

    fun onRemoveIngredientFromForm(ingredientId: String) {
        val current = _formState.value.formIngredients
        _formState.value = _formState.value.copy(
            formIngredients = current.filter { it.ingredientId != ingredientId },
        )
    }

    fun onSaveRecipe() {
        viewModelScope.launch {
            _formState.value = _formState.value.copy(isSaving = true, error = null)
            try {
                val state = _formState.value
                val existing = state.editingRecipe

                if (existing != null) {
                    recipeRepository.updateRecipe(
                        existing.copy(
                            name = state.formName,
                            description = state.formDescription,
                            category = state.formCategory,
                            ingredients = state.formIngredients,
                            isActive = state.formIsActive,
                        ),
                    )
                } else {
                    recipeRepository.addRecipe(
                        Recipe(
                            restaurantId = restaurantId,
                            name = state.formName,
                            description = state.formDescription,
                            category = state.formCategory,
                            ingredients = state.formIngredients,
                            isActive = state.formIsActive,
                        ),
                    )
                }
                onDismissEditor()
            } catch (e: Exception) {
                _formState.value = _formState.value.copy(
                    isSaving = false,
                    error = e.message ?: "Error al guardar receta",
                )
            }
        }
    }

    fun onDeleteRecipe(id: String) {
        viewModelScope.launch {
            try {
                recipeRepository.deleteRecipe(id)
                onDismissEditor()
            } catch (e: Exception) {
                _formState.value = _formState.value.copy(
                    error = e.message ?: "Error al eliminar receta",
                )
            }
        }
    }
}
