package org.apptolast.menuadmin.data.remote.consumer

import kotlinx.serialization.Serializable
import org.apptolast.menuadmin.data.remote.restaurant.RestaurantResponseDto

@Serializable
data class PageResponseDto<T>(
    val content: List<T>,
    val page: Int = 0,
    val size: Int = 20,
    val totalElements: Long = 0,
    val totalPages: Int = 0,
    val last: Boolean = true,
)

@Serializable
data class AllergenProfileResponseDto(
    val profileUuid: String = "",
    val allergenCodes: List<String> = emptyList(),
    val severityNotes: String = "",
    val updatedAt: String? = null,
)

@Serializable
data class AllergenProfileRequestDto(
    val allergenCodes: List<String>,
    val severityNotes: String = "",
)

@Serializable
data class DataExportResponseDto(
    val userId: String = "",
    val email: String = "",
    val role: String = "",
    val allergenProfile: AllergenProfileExportDto? = null,
    val exportedAt: String? = null,
)

@Serializable
data class AllergenProfileExportDto(
    val allergenCodes: List<String> = emptyList(),
    val severityNotes: String = "",
)

// Type alias for paginated restaurant response
typealias RestaurantPageResponseDto = PageResponseDto<RestaurantResponseDto>
