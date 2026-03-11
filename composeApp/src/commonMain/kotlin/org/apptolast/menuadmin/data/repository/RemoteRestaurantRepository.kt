package org.apptolast.menuadmin.data.repository

import org.apptolast.menuadmin.data.remote.mapper.toDomain
import org.apptolast.menuadmin.data.remote.restaurant.RestaurantRequestDto
import org.apptolast.menuadmin.data.remote.restaurant.RestaurantService
import org.apptolast.menuadmin.domain.model.Restaurant
import org.apptolast.menuadmin.domain.repository.RestaurantRepository

class RemoteRestaurantRepository(
    private val restaurantService: RestaurantService,
) : RestaurantRepository {
    override suspend fun getRestaurant(): Restaurant = restaurantService.getRestaurant().toDomain()

    override suspend fun updateRestaurant(restaurant: Restaurant): Restaurant =
        restaurantService.updateRestaurant(
            RestaurantRequestDto(
                name = restaurant.name,
                slug = restaurant.slug,
                description = restaurant.description.ifEmpty { null },
                address = restaurant.address.ifEmpty { null },
                phone = restaurant.phone.ifEmpty { null },
                logoUrl = restaurant.logoUrl,
            ),
        ).toDomain()
}
