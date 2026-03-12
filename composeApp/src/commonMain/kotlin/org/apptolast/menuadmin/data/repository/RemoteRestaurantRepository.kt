package org.apptolast.menuadmin.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import org.apptolast.menuadmin.data.remote.mapper.toDomain
import org.apptolast.menuadmin.data.remote.restaurant.RestaurantRequestDto
import org.apptolast.menuadmin.data.remote.restaurant.RestaurantService
import org.apptolast.menuadmin.domain.model.Restaurant
import org.apptolast.menuadmin.domain.repository.RestaurantRepository

class RemoteRestaurantRepository(
    private val restaurantService: RestaurantService,
) : RestaurantRepository {
    private val _restaurants = MutableStateFlow<List<Restaurant>>(emptyList())
    private var hasLoaded = false

    override fun getAllRestaurants(): Flow<List<Restaurant>> =
        flow {
            if (!hasLoaded) {
                try {
                    refreshRestaurants()
                } catch (_: Exception) {
                }
            }
            emitAll(_restaurants)
        }

    private suspend fun refreshRestaurants() {
        val restaurant = restaurantService.getRestaurant().toDomain()
        _restaurants.value = listOf(restaurant)
        hasLoaded = true
    }

    override suspend fun getRestaurantById(id: String): Restaurant? {
        if (!hasLoaded) refreshRestaurants()
        return _restaurants.value.find { it.id == id }
    }

    override suspend fun createRestaurant(restaurant: Restaurant): Restaurant {
        error(
            "La creación de restaurante se realiza durante el registro (register-restaurant). Usa updateRestaurant() para modificar el existente.",
        )
    }

    override suspend fun updateRestaurant(restaurant: Restaurant): Restaurant {
        val updated = restaurantService.updateRestaurant(restaurant.toRequest()).toDomain()
        refreshRestaurants()
        return updated
    }

    override suspend fun deleteRestaurant(id: String) {
        // Not supported in single-tenant mode
    }

    private fun Restaurant.toRequest() =
        RestaurantRequestDto(
            name = name,
            slug = slug,
            description = description.ifEmpty { null },
            address = address.ifEmpty { null },
            phone = phone.ifEmpty { null },
            logoUrl = logoUrl,
        )
}
