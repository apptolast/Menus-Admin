package org.apptolast.menuadmin.presentation.screens.menus

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
import org.apptolast.menuadmin.domain.model.DishCategory
import org.apptolast.menuadmin.domain.model.Menu
import org.apptolast.menuadmin.domain.model.MenuRecipeSummary
import org.apptolast.menuadmin.domain.repository.MenuRepository
import org.apptolast.menuadmin.domain.repository.RecipeRepository
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MenusViewModel(
    private val menuRepository: MenuRepository,
    private val recipeRepository: RecipeRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val restaurantId: String = savedStateHandle["restaurantId"] ?: ""

    private val _localState = MutableStateFlow(MenusUiState())

    val uiState: StateFlow<MenusUiState> = combine(
        menuRepository.getMenusByRestaurant(restaurantId),
        recipeRepository.getRecipesByRestaurant(restaurantId),
        _localState,
    ) { menus, recipes, localState ->
        val updatedSelectedMenu = localState.selectedMenu?.let { selected ->
            menus.find { it.id == selected.id }
        }
        localState.copy(
            isLoading = false,
            menus = menus,
            availableRecipes = recipes,
            selectedMenu = updatedSelectedMenu ?: localState.selectedMenu,
        )
    }
        .catch { throwable ->
            emit(
                _localState.value.copy(
                    isLoading = false,
                    error = throwable.message ?: "Error al cargar menus",
                ),
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MenusUiState(),
        )

    fun selectMenu(menu: Menu) {
        _localState.value = _localState.value.copy(
            selectedMenu = menu,
            selectedCategory = null,
        )
    }

    fun clearMenuSelection() {
        _localState.value = _localState.value.copy(
            selectedMenu = null,
            selectedCategory = null,
        )
    }

    fun filterByCategory(category: DishCategory?) {
        _localState.value = _localState.value.copy(selectedCategory = category)
    }

    fun onNewMenu() {
        _localState.value = _localState.value.copy(
            isFormVisible = true,
            formName = "",
            formDescription = "",
            formRestaurantLogoUrl = "",
            formCompanyLogoUrl = "",
            formSelectedRecipeIds = emptySet(),
        )
    }

    fun onFormNameChange(name: String) {
        _localState.value = _localState.value.copy(formName = name)
    }

    fun onFormDescriptionChange(desc: String) {
        _localState.value = _localState.value.copy(formDescription = desc)
    }

    fun onFormRestaurantLogoUrlChange(url: String) {
        _localState.value = _localState.value.copy(formRestaurantLogoUrl = url)
    }

    fun onFormCompanyLogoUrlChange(url: String) {
        _localState.value = _localState.value.copy(formCompanyLogoUrl = url)
    }

    fun onToggleRecipeSelection(recipeId: String) {
        val current = _localState.value.formSelectedRecipeIds
        val updated = if (recipeId in current) current - recipeId else current + recipeId
        _localState.value = _localState.value.copy(formSelectedRecipeIds = updated)
    }

    fun onDismissForm() {
        _localState.value = _localState.value.copy(
            isFormVisible = false,
            formName = "",
            formDescription = "",
            formRestaurantLogoUrl = "",
            formCompanyLogoUrl = "",
            formSelectedRecipeIds = emptySet(),
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    fun onSaveMenu() {
        viewModelScope.launch {
            try {
                _localState.value = _localState.value.copy(isSaving = true)
                val state = _localState.value
                val now = Clock.System.now()

                val selectedRecipes = state.availableRecipes
                    .filter { it.id in state.formSelectedRecipeIds }
                    .map { MenuRecipeSummary(id = it.id, name = it.name) }

                menuRepository.addMenu(
                    Menu(
                        id = Uuid.random().toString(),
                        restaurantId = restaurantId,
                        name = state.formName,
                        description = state.formDescription,
                        restaurantLogoUrl = state.formRestaurantLogoUrl.ifBlank { null },
                        companyLogoUrl = state.formCompanyLogoUrl.ifBlank { null },
                        recipes = selectedRecipes,
                        createdAt = now,
                        updatedAt = now,
                    ),
                )
                _localState.value = _localState.value.copy(
                    isSaving = false,
                    isFormVisible = false,
                    formName = "",
                    formDescription = "",
                    formRestaurantLogoUrl = "",
                    formCompanyLogoUrl = "",
                    formSelectedRecipeIds = emptySet(),
                )
            } catch (e: Exception) {
                _localState.value = _localState.value.copy(
                    isSaving = false,
                    error = e.message ?: "Error al crear menu",
                )
            }
        }
    }

    fun exportJson() {
        viewModelScope.launch {
            try {
                val menu = _localState.value.selectedMenu ?: return@launch
                menuRepository.exportMenuToJson(menu.id)
            } catch (e: Exception) {
                _localState.value = _localState.value.copy(
                    error = e.message ?: "Error al exportar JSON",
                )
            }
        }
    }

    fun exportPdf() {
        viewModelScope.launch {
            try {
                // PDF export would be handled by platform-specific code
            } catch (e: Exception) {
                _localState.value = _localState.value.copy(
                    error = e.message ?: "Error al exportar PDF",
                )
            }
        }
    }
}
