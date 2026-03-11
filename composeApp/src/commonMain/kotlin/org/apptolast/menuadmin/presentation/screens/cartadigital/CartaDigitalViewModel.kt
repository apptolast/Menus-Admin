package org.apptolast.menuadmin.presentation.screens.cartadigital

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.apptolast.menuadmin.domain.model.AllergenType
import org.apptolast.menuadmin.domain.repository.MenuRepository

class CartaDigitalViewModel(
    menuRepository: MenuRepository,
) : ViewModel() {
    private val _selectedAllergens = MutableStateFlow<Set<AllergenType>>(emptySet())
    private val _selectedMenuId = MutableStateFlow<String?>(null)

    val uiState: StateFlow<CartaDigitalUiState> = combine(
        menuRepository.getAllMenus(),
        _selectedAllergens,
        _selectedMenuId,
    ) { menus, allergens, menuId ->
        val effectiveMenuId = menuId ?: menus.firstOrNull()?.id
        val selectedMenu = menus.find { it.id == effectiveMenuId }
        val dishes = selectedMenu?.dishes.orEmpty()

        val (safe, unsafe) = if (allergens.isEmpty()) {
            dishes to emptyList()
        } else {
            dishes.partition { dish ->
                dish.allergens.none { it in allergens }
            }
        }

        CartaDigitalUiState(
            isLoading = false,
            availableMenus = menus,
            selectedMenuId = effectiveMenuId,
            selectedAllergens = allergens,
            allDishes = dishes,
            safeDishes = safe,
            unsafeDishes = unsafe,
        )
    }
        .catch { throwable ->
            emit(
                CartaDigitalUiState(
                    isLoading = false,
                    error = throwable.message ?: "Error al cargar carta digital",
                ),
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CartaDigitalUiState(),
        )

    fun onToggleAllergen(allergen: AllergenType) {
        val current = _selectedAllergens.value
        _selectedAllergens.value = if (allergen in current) current - allergen else current + allergen
    }

    fun onSelectMenu(menuId: String) {
        _selectedMenuId.value = menuId
    }

    fun onClearAllergens() {
        _selectedAllergens.value = emptySet()
    }
}
