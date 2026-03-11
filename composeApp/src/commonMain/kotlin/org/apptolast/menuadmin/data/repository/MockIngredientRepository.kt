package org.apptolast.menuadmin.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.apptolast.menuadmin.data.local.MockDataProvider
import org.apptolast.menuadmin.domain.model.Ingredient
import org.apptolast.menuadmin.domain.repository.IngredientRepository

class MockIngredientRepository : IngredientRepository {
    private val _ingredients = MutableStateFlow(MockDataProvider.ingredients)

    override fun getAllIngredients(): Flow<List<Ingredient>> = _ingredients.asStateFlow()

    override suspend fun getIngredientById(id: String): Ingredient? {
        return _ingredients.value.find { it.id == id }
    }

    override suspend fun addIngredient(ingredient: Ingredient): Ingredient {
        _ingredients.update { current -> current + ingredient }
        return ingredient
    }

    override suspend fun updateIngredient(ingredient: Ingredient): Ingredient {
        _ingredients.update { current ->
            current.map { if (it.id == ingredient.id) ingredient else it }
        }
        return ingredient
    }

    override suspend fun deleteIngredient(id: String) {
        _ingredients.update { current -> current.filter { it.id != id } }
    }

    override suspend fun searchIngredients(query: String): List<Ingredient> {
        if (query.isBlank()) return _ingredients.value
        return _ingredients.value.filter {
            it.name.contains(query, ignoreCase = true)
        }
    }

    override suspend fun replaceAll(ingredients: List<Ingredient>) {
        _ingredients.value = ingredients
    }
}
