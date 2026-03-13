package org.apptolast.menuadmin.data.repository

import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import org.apptolast.menuadmin.data.remote.ingredient.IngredientAllergenRequestDto
import org.apptolast.menuadmin.data.remote.ingredient.IngredientRequestDto
import org.apptolast.menuadmin.data.remote.ingredient.IngredientService
import org.apptolast.menuadmin.data.remote.ingredient.UpdateIngredientAllergensRequestDto
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
                } catch (e: ClientRequestException) {
                    if (e.response.status == HttpStatusCode.BadRequest) {
                        // TENANT_REQUIRED or similar
                    } else {
                        throw e
                    }
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
        // Step 1: Create the ingredient (without allergens)
        val response = ingredientService.createIngredient(ingredient.toRequest())
        val created = response.toDomain()

        // Step 2: Update allergens if any
        if (ingredient.allergens.isNotEmpty()) {
            ingredientService.updateIngredientAllergens(
                created.id,
                UpdateIngredientAllergensRequestDto(
                    allergens = ingredient.allergens.map {
                        IngredientAllergenRequestDto(
                            allergenCode = it.allergenCode,
                            containmentLevel = it.containmentLevel.apiValue,
                        )
                    },
                ),
            )
        }

        refreshIngredients()
        return _ingredients.value.find { it.id == created.id } ?: created
    }

    override suspend fun updateIngredient(ingredient: Ingredient): Ingredient {
        // Step 1: Update ingredient data
        ingredientService.updateIngredient(ingredient.id, ingredient.toRequest())

        // Step 2: Update allergens
        ingredientService.updateIngredientAllergens(
            ingredient.id,
            UpdateIngredientAllergensRequestDto(
                allergens = ingredient.allergens.map {
                    IngredientAllergenRequestDto(
                        allergenCode = it.allergenCode,
                        containmentLevel = it.containmentLevel.apiValue,
                    )
                },
            ),
        )

        refreshIngredients()
        return _ingredients.value.find { it.id == ingredient.id } ?: ingredient
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
            description = description.ifEmpty { null },
            brand = brand.ifEmpty { null },
            labelInfo = labelInfo.ifEmpty { null },
        )
}
