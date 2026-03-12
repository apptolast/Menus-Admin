package org.apptolast.menuadmin.presentation.screens.ingredients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.apptolast.menuadmin.domain.model.AllergenType
import org.apptolast.menuadmin.domain.model.Ingredient
import org.apptolast.menuadmin.domain.repository.IngredientRepository
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class IngredientsViewModel(
    private val ingredientRepository: IngredientRepository,
) : ViewModel() {
    private val _formState = MutableStateFlow(IngredientsUiState())

    val uiState: StateFlow<IngredientsUiState> = combine(
        ingredientRepository.getAllIngredients(),
        _formState,
    ) { ingredients, formState ->
        var filtered = ingredients
        if (formState.searchQuery.isNotBlank()) {
            filtered = filtered.filter { ingredient ->
                ingredient.name.contains(formState.searchQuery, ignoreCase = true) ||
                    ingredient.brand.contains(formState.searchQuery, ignoreCase = true)
            }
        }
        if (formState.filterAllergens.isNotEmpty()) {
            filtered = filtered.filter { ingredient ->
                ingredient.allergens.any { it in formState.filterAllergens }
            }
        }
        formState.copy(
            isLoading = false,
            ingredients = filtered,
        )
    }
        .catch { throwable ->
            emit(
                _formState.value.copy(
                    isLoading = false,
                    error = throwable.message ?: "Error al cargar ingredientes",
                ),
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = IngredientsUiState(),
        )

    fun onSearchQueryChange(query: String) {
        _formState.value = _formState.value.copy(searchQuery = query)
    }

    fun onToggleAllergenFilter(allergen: AllergenType) {
        val current = _formState.value.filterAllergens
        val updated = if (allergen in current) current - allergen else current + allergen
        _formState.value = _formState.value.copy(filterAllergens = updated)
    }

    fun onClearAllergenFilters() {
        _formState.value = _formState.value.copy(filterAllergens = emptySet())
    }

    fun onNewIngredient() {
        _formState.value = _formState.value.copy(
            isEditing = true,
            editingIngredient = null,
            formName = "",
            formBrand = "",
            formSupplier = "",
            formOcrRawText = "",
            formNotes = "",
            formAllergens = emptySet(),
            formTraces = emptySet(),
            error = null,
        )
    }

    fun onEditIngredient(ingredient: Ingredient) {
        _formState.value = _formState.value.copy(
            isEditing = true,
            editingIngredient = ingredient,
            formName = ingredient.name,
            formBrand = ingredient.brand,
            formSupplier = ingredient.supplier,
            formOcrRawText = ingredient.ocrRawText,
            formNotes = ingredient.notes,
            formAllergens = ingredient.allergens,
            formTraces = ingredient.traces,
            error = null,
        )
    }

    fun onDismissEditor() {
        _formState.value = _formState.value.copy(
            isEditing = false,
            isSaving = false,
            editingIngredient = null,
            formName = "",
            formBrand = "",
            formSupplier = "",
            formOcrRawText = "",
            formNotes = "",
            formAllergens = emptySet(),
            formTraces = emptySet(),
            error = null,
        )
    }

    fun onFormNameChange(value: String) {
        _formState.value = _formState.value.copy(formName = value)
    }

    fun onFormBrandChange(value: String) {
        _formState.value = _formState.value.copy(formBrand = value)
    }

    fun onFormSupplierChange(value: String) {
        _formState.value = _formState.value.copy(formSupplier = value)
    }

    fun onFormOcrRawTextChange(value: String) {
        _formState.value = _formState.value.copy(formOcrRawText = value)
    }

    fun onFormNotesChange(value: String) {
        _formState.value = _formState.value.copy(formNotes = value)
    }

    fun onToggleAllergen(allergen: AllergenType) {
        val current = _formState.value.formAllergens
        val updated = if (allergen in current) current - allergen else current + allergen
        _formState.value = _formState.value.copy(formAllergens = updated)
    }

    fun onToggleTrace(allergen: AllergenType) {
        val current = _formState.value.formTraces
        val updated = if (allergen in current) current - allergen else current + allergen
        _formState.value = _formState.value.copy(formTraces = updated)
    }

    @OptIn(ExperimentalUuidApi::class)
    fun onSaveIngredient() {
        viewModelScope.launch {
            _formState.value = _formState.value.copy(isSaving = true, error = null)
            try {
                val state = _formState.value
                val now = Clock.System.now()
                val existing = state.editingIngredient

                if (existing != null) {
                    ingredientRepository.updateIngredient(
                        existing.copy(
                            name = state.formName,
                            brand = state.formBrand,
                            supplier = state.formSupplier,
                            ocrRawText = state.formOcrRawText,
                            notes = state.formNotes,
                            allergens = state.formAllergens,
                            traces = state.formTraces,
                            updatedAt = now,
                        ),
                    )
                } else {
                    ingredientRepository.addIngredient(
                        Ingredient(
                            id = Uuid.random().toString(),
                            name = state.formName,
                            brand = state.formBrand,
                            supplier = state.formSupplier,
                            ocrRawText = state.formOcrRawText,
                            notes = state.formNotes,
                            allergens = state.formAllergens,
                            traces = state.formTraces,
                            createdAt = now,
                            updatedAt = now,
                        ),
                    )
                }
                onDismissEditor()
            } catch (e: Exception) {
                _formState.value = _formState.value.copy(
                    isSaving = false,
                    error = e.message ?: "Error al guardar ingrediente",
                )
            }
        }
    }

    fun onDeleteIngredient(id: String) {
        viewModelScope.launch {
            try {
                ingredientRepository.deleteIngredient(id)
                onDismissEditor()
            } catch (e: Exception) {
                _formState.value = _formState.value.copy(
                    error = e.message ?: "Error al eliminar ingrediente",
                )
            }
        }
    }

    fun onDismissError() {
        _formState.value = _formState.value.copy(error = null)
    }
}
