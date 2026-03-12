package org.apptolast.menuadmin.data.remote.restaurant

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import org.apptolast.menuadmin.data.remote.ApiConstants

class RestaurantService(
    private val client: HttpClient,
) {
    // Single-tenant: admin has one restaurant
    suspend fun getRestaurant(): RestaurantResponseDto = client.get(ApiConstants.ADMIN_RESTAURANT).body()

    suspend fun updateRestaurant(request: RestaurantRequestDto): RestaurantResponseDto =
        client.put(ApiConstants.ADMIN_RESTAURANT) {
            setBody(request)
        }.body()
}
