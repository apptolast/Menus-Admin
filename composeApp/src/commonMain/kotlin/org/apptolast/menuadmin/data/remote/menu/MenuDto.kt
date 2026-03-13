package org.apptolast.menuadmin.data.remote.menu

import kotlinx.serialization.Serializable

@Serializable
data class MenuResponseDto(
    val id: String,
    val name: String,
    val description: String = "",
    val displayOrder: Int = 0,
    val published: Boolean = false,
    val archived: Boolean = false,
    val sections: List<SectionResponseDto> = emptyList(),
    val updatedAt: String? = null,
)

@Serializable
data class SectionResponseDto(
    val id: String,
    val name: String,
    val displayOrder: Int = 0,
)

@Serializable
data class MenuRequestDto(
    val name: String,
    val description: String? = null,
    val displayOrder: Int? = null,
)

@Serializable
data class SectionRequestDto(
    val name: String,
    val displayOrder: Int? = null,
)

@Serializable
data class MenuPublishRequestDto(
    val published: Boolean,
)

@Serializable
data class MenuRecipeRequestDto(
    val recipeId: String,
    val section: String? = null,
    val sortOrder: Int? = null,
)
