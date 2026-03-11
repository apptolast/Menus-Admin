package org.apptolast.menuadmin.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.apptolast.menuadmin.data.local.MockDataProvider
import org.apptolast.menuadmin.domain.model.Recipe
import org.apptolast.menuadmin.domain.repository.RecipeRepository
import kotlin.time.Clock

class MockRecipeRepository : RecipeRepository {
    private val _recipes = MutableStateFlow(MockDataProvider.recipes)

    override fun getAllRecipes(): Flow<List<Recipe>> = _recipes.asStateFlow()

    override suspend fun getRecipeById(id: String): Recipe? {
        return _recipes.value.find { it.id == id }
    }

    override suspend fun addRecipe(recipe: Recipe): Recipe {
        _recipes.update { current -> current + recipe }
        return recipe
    }

    override suspend fun updateRecipe(recipe: Recipe): Recipe {
        _recipes.update { current ->
            current.map { if (it.id == recipe.id) recipe else it }
        }
        return recipe
    }

    override suspend fun deleteRecipe(id: String) {
        _recipes.update { current -> current.filter { it.id != id } }
    }

    override suspend fun toggleRecipeActive(id: String): Recipe {
        var toggled: Recipe? = null
        _recipes.update { current ->
            current.map {
                if (it.id == id) {
                    it.copy(
                        isActive = !it.isActive,
                        updatedAt = Clock.System.now(),
                    ).also { updated -> toggled = updated }
                } else {
                    it
                }
            }
        }
        return toggled ?: throw NoSuchElementException("Recipe with id $id not found")
    }

    override suspend fun replaceAll(recipes: List<Recipe>) {
        _recipes.value = recipes
    }
}
