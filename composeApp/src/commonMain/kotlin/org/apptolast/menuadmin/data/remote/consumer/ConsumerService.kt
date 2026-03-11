package org.apptolast.menuadmin.data.remote.consumer

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import org.apptolast.menuadmin.data.remote.ApiConstants
import org.apptolast.menuadmin.data.remote.dish.DishResponseDto
import org.apptolast.menuadmin.data.remote.menu.MenuResponseDto
import org.apptolast.menuadmin.data.remote.restaurant.RestaurantResponseDto

class ConsumerService(
    private val client: HttpClient,
) {
    // Public restaurants
    suspend fun searchRestaurants(
        name: String? = null,
        page: Int = 0,
        size: Int = 20,
    ): RestaurantPageResponseDto =
        client.get(ApiConstants.RESTAURANTS) {
            name?.let { parameter("name", it) }
            parameter("page", page)
            parameter("size", size)
        }.body()

    suspend fun getRestaurant(id: String): RestaurantResponseDto = client.get("${ApiConstants.RESTAURANTS}/$id").body()

    // Public menus
    suspend fun getRestaurantMenus(restaurantId: String): List<MenuResponseDto> =
        client.get("${ApiConstants.RESTAURANTS}/$restaurantId/menu").body()

    // Public dishes (with semáforo if authenticated + consented)
    suspend fun getSectionDishes(
        restaurantId: String,
        sectionId: String,
    ): List<DishResponseDto> = client.get("${ApiConstants.RESTAURANTS}/$restaurantId/sections/$sectionId/dishes").body()

    // User allergen profile
    suspend fun getAllergenProfile(): AllergenProfileResponseDto = client.get(ApiConstants.USER_ALLERGEN_PROFILE).body()

    suspend fun updateAllergenProfile(request: AllergenProfileRequestDto): AllergenProfileResponseDto =
        client.put(ApiConstants.USER_ALLERGEN_PROFILE) {
            setBody(request)
        }.body()

    // GDPR
    suspend fun exportData(): DataExportResponseDto = client.get(ApiConstants.USER_DATA_EXPORT).body()

    suspend fun deleteAccount() {
        client.delete(ApiConstants.USER_DATA_DELETE)
    }
}
