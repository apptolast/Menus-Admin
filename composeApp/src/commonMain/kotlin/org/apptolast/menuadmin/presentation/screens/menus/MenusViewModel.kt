package org.apptolast.menuadmin.presentation.screens.menus

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
import org.apptolast.menuadmin.domain.repository.MenuRepository

class MenusViewModel(
    private val menuRepository: MenuRepository,
) : ViewModel() {
    private val _localState = MutableStateFlow(MenusUiState())

    val uiState: StateFlow<MenusUiState> = combine(
        menuRepository.getAllMenus(),
        _localState,
    ) { menus, localState ->
        // Keep selectedMenu in sync if it was updated in the repository
        val updatedSelectedMenu = localState.selectedMenu?.let { selected ->
            menus.find { it.id == selected.id }
        }
        localState.copy(
            isLoading = false,
            menus = menus,
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

    fun exportJson() {
        viewModelScope.launch {
            try {
                val menu = _localState.value.selectedMenu ?: return@launch
                menuRepository.exportMenuToJson(menu.id)
                // In a real app, this would trigger a file save dialog or share intent
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
                // This is a placeholder for the action
            } catch (e: Exception) {
                _localState.value = _localState.value.copy(
                    error = e.message ?: "Error al exportar PDF",
                )
            }
        }
    }
}
