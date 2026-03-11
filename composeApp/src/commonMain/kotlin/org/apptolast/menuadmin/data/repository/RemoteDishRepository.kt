package org.apptolast.menuadmin.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import org.apptolast.menuadmin.data.remote.dish.DishAllergenRequestDto
import org.apptolast.menuadmin.data.remote.dish.DishRequestDto
import org.apptolast.menuadmin.data.remote.dish.DishService
import org.apptolast.menuadmin.data.remote.mapper.toDomain
import org.apptolast.menuadmin.domain.model.ContainmentLevel
import org.apptolast.menuadmin.domain.model.Dish
import org.apptolast.menuadmin.domain.repository.DishRepository

class RemoteDishRepository(
    private val dishService: DishService,
) : DishRepository {
    private val _dishes = MutableStateFlow<List<Dish>>(emptyList())
    private var hasLoaded = false

    override fun getAllDishes(): Flow<List<Dish>> =
        flow {
            if (!hasLoaded) {
                try {
                    refreshDishes()
                } catch (_: Exception) {
                    // Emit empty list on failure
                }
            }
            emitAll(_dishes)
        }

    suspend fun refreshDishes() {
        _dishes.value = dishService.getDishes().map { it.toDomain() }
        hasLoaded = true
    }

    override suspend fun createDish(
        name: String,
        sectionId: String,
        description: String,
        price: Double,
        imageUrl: String?,
        allergens: List<Pair<String, ContainmentLevel>>,
    ): Dish {
        val response = dishService.createDish(
            DishRequestDto(
                name = name,
                sectionId = sectionId,
                description = description.ifEmpty { null },
                price = if (price > 0) price else null,
                imageUrl = imageUrl,
                allergens = allergens.map { (code, level) ->
                    DishAllergenRequestDto(
                        allergenCode = code,
                        containmentLevel = level.apiValue,
                    )
                }.ifEmpty { null },
            ),
        )
        refreshDishes()
        return response.toDomain()
    }

    override suspend fun updateDish(
        id: String,
        name: String,
        sectionId: String,
        description: String,
        price: Double,
        imageUrl: String?,
        allergens: List<Pair<String, ContainmentLevel>>,
    ): Dish {
        val response = dishService.updateDish(
            id,
            DishRequestDto(
                name = name,
                sectionId = sectionId,
                description = description.ifEmpty { null },
                price = if (price > 0) price else null,
                imageUrl = imageUrl,
                allergens = allergens.map { (code, level) ->
                    DishAllergenRequestDto(
                        allergenCode = code,
                        containmentLevel = level.apiValue,
                    )
                }.ifEmpty { null },
            ),
        )
        refreshDishes()
        return response.toDomain()
    }

    override suspend fun deleteDish(id: String) {
        dishService.deleteDish(id)
        refreshDishes()
    }

    override suspend fun addAllergen(
        dishId: String,
        allergenCode: String,
        level: ContainmentLevel,
        notes: String,
    ): Dish {
        val response = dishService.addAllergen(
            dishId,
            DishAllergenRequestDto(
                allergenCode = allergenCode,
                containmentLevel = level.apiValue,
                notes = notes.ifEmpty { null },
            ),
        )
        refreshDishes()
        return response.toDomain()
    }

    override suspend fun removeAllergen(
        dishId: String,
        allergenId: Int,
    ) {
        dishService.removeAllergen(dishId, allergenId)
        refreshDishes()
    }
}
