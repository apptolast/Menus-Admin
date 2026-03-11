package org.apptolast.menuadmin.domain.repository

import org.apptolast.menuadmin.domain.model.Restaurant

interface RestaurantRepository {
    suspend fun getRestaurant(): Restaurant

    suspend fun updateRestaurant(restaurant: Restaurant): Restaurant
}
