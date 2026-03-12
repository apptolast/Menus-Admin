package org.apptolast.menuadmin.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import org.apptolast.menuadmin.data.remote.ingredient.IngredientRequestDto
import org.apptolast.menuadmin.data.remote.ingredient.IngredientService
import org.apptolast.menuadmin.data.remote.mapper.toDomain
import org.apptolast.menuadmin.domain.model.Ingredient
import org.apptolast.menuadmin.domain.repository.IngredientRepository

class RemoteIngredientRepository(
    private val ingredientService: IngredientService,
) : IngredientRepository {
    private val _ingredients = MutableStateFlow<List<Ingredient>>(emptyList())
    private var hasLoaded = false

    override fun getAllIngredients(): Flow<List<Ingredient>> =
        flow {
            if (!hasLoaded) {
                try {
                    refreshIngredients()
                } catch (_: Exception) {
                }
            }
            emitAll(_ingredients)
        }

    private suspend fun refreshIngredients() {
        _ingredients.value = ingredientService.getIngredients().map { it.toDomain() }
        hasLoaded = true
    }

    override suspend fun getIngredientById(id: String): Ingredient? {
        if (!hasLoaded) refreshIngredients()
        return _ingredients.value.find { it.id == id }
    }

    override suspend fun addIngredient(ingredient: Ingredient): Ingredient {
        val response = ingredientService.createIngredient(ingredient.toRequest())
        val created = response.toDomain()
        refreshIngredients()
        return created
    }

    override suspend fun updateIngredient(ingredient: Ingredient): Ingredient {
        val response = ingredientService.updateIngredient(ingredient.id, ingredient.toRequest())
        val updated = response.toDomain()
        refreshIngredients()
        return updated
    }

    override suspend fun deleteIngredient(id: String) {
        ingredientService.deleteIngredient(id)
        refreshIngredients()
    }

    override suspend fun searchIngredients(query: String): List<Ingredient> {
        return ingredientService.searchIngredients(query).map { it.toDomain() }
    }

    override suspend fun replaceAll(ingredients: List<Ingredient>) {
        // Not applicable for remote — API is the source of truth
        refreshIngredients()
    }

    private fun Ingredient.toRequest() =
        IngredientRequestDto(
            name = name,
            brand = brand.ifEmpty { null },
            supplier = supplier.ifEmpty { null },
            allergens = allergens.map { it.apiCode },
            traces = traces.map { it.apiCode },
            ocrRawText = ocrRawText.ifEmpty { null },
            notes = notes.ifEmpty { null },
        )
}
