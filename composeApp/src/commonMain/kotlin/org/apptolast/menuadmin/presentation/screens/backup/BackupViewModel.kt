package org.apptolast.menuadmin.presentation.screens.backup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.apptolast.menuadmin.domain.platform.FileHandler
import org.apptolast.menuadmin.domain.repository.IngredientRepository
import org.apptolast.menuadmin.domain.repository.MenuRepository
import org.apptolast.menuadmin.domain.repository.RecipeRepository
import org.apptolast.menuadmin.domain.util.JsonExporter

data class BackupUiState(
    val isExporting: Boolean = false,
    val isImporting: Boolean = false,
    val message: String? = null,
)

class BackupViewModel(
    private val ingredientRepository: IngredientRepository,
    private val recipeRepository: RecipeRepository,
    private val menuRepository: MenuRepository,
    private val fileHandler: FileHandler,
    private val json: Json,
) : ViewModel() {
    private val _uiState = MutableStateFlow(BackupUiState())
    val uiState: StateFlow<BackupUiState> = _uiState.asStateFlow()

    fun exportData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isExporting = true, message = null) }
            try {
                val ingredients = ingredientRepository.getAllIngredients().first()
                val recipes = recipeRepository.getAllRecipes().first()
                val menus = menuRepository.getAllMenus().first()
                val jsonString = JsonExporter.exportAllData(ingredients, recipes, menus, json)
                fileHandler.saveFile(jsonString, "menuadmin_backup.json")
                _uiState.update {
                    it.copy(
                        isExporting = false,
                        message = "Exportacion completada: ${ingredients.size} ingredientes, " +
                            "${recipes.size} recetas, ${menus.size} menus",
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isExporting = false, message = "Error al exportar: ${e.message}")
                }
            }
        }
    }

    fun importData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isImporting = true, message = null) }
            try {
                val content = fileHandler.pickAndReadFile()
                if (content == null) {
                    _uiState.update { it.copy(isImporting = false) }
                    return@launch
                }
                val result = JsonExporter.importExternalData(content, json)
                ingredientRepository.replaceAll(result.ingredients)
                recipeRepository.replaceAll(result.recipes)
                _uiState.update {
                    it.copy(
                        isImporting = false,
                        message = "Importacion completada: " +
                            "${result.ingredients.size} ingredientes, " +
                            "${result.recipes.size} recetas",
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isImporting = false, message = "Error al importar: ${e.message}")
                }
            }
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(message = null) }
    }
}
