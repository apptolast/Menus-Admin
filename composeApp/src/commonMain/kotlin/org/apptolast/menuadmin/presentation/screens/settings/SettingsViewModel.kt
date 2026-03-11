package org.apptolast.menuadmin.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.apptolast.menuadmin.domain.model.Restaurant
import org.apptolast.menuadmin.domain.repository.RestaurantRepository

class SettingsViewModel(
    private val restaurantRepository: RestaurantRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadRestaurant()
    }

    private fun loadRestaurant() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val restaurant = restaurantRepository.getRestaurant()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        restaurant = restaurant,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar el restaurante: ${e.message ?: "Error desconocido"}",
                    )
                }
            }
        }
    }

    fun startEditing() {
        val restaurant = _uiState.value.restaurant ?: return
        _uiState.update {
            it.copy(
                isEditing = true,
                editName = restaurant.name,
                editDescription = restaurant.description,
                editAddress = restaurant.address,
                editPhone = restaurant.phone,
            )
        }
    }

    fun cancelEditing() {
        _uiState.update { it.copy(isEditing = false) }
    }

    fun onNameChange(name: String) {
        _uiState.update { it.copy(editName = name) }
    }

    fun onDescriptionChange(description: String) {
        _uiState.update { it.copy(editDescription = description) }
    }

    fun onAddressChange(address: String) {
        _uiState.update { it.copy(editAddress = address) }
    }

    fun onPhoneChange(phone: String) {
        _uiState.update { it.copy(editPhone = phone) }
    }

    fun saveRestaurant() {
        val state = _uiState.value
        val current = state.restaurant ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            try {
                val updated = restaurantRepository.updateRestaurant(
                    Restaurant(
                        id = current.id,
                        name = state.editName,
                        slug = current.slug,
                        description = state.editDescription,
                        address = state.editAddress,
                        phone = state.editPhone,
                        logoUrl = current.logoUrl,
                        active = current.active,
                    ),
                )
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        isEditing = false,
                        restaurant = updated,
                        successMessage = "Restaurante actualizado",
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = "Error al guardar: ${e.message ?: "Error desconocido"}",
                    )
                }
            }
        }
    }

    fun dismissMessage() {
        _uiState.update { it.copy(error = null, successMessage = null) }
    }
}
