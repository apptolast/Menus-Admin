package org.apptolast.menuadmin.data.remote.allergen

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.apptolast.menuadmin.data.remote.ApiConstants

class AllergenService(
    private val client: HttpClient,
) {
    suspend fun getAllergens(): List<AllergenResponseDto> = client.get(ApiConstants.ALLERGENS).body()
}
