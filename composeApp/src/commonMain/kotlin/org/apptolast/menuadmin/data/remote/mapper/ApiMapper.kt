package org.apptolast.menuadmin.data.remote.mapper

import org.apptolast.menuadmin.data.remote.dish.DishAllergenResponseDto
import org.apptolast.menuadmin.data.remote.dish.DishResponseDto
import org.apptolast.menuadmin.data.remote.ingredient.IngredientResponseDto
import org.apptolast.menuadmin.data.remote.menu.MenuResponseDto
import org.apptolast.menuadmin.data.remote.menu.SectionResponseDto
import org.apptolast.menuadmin.data.remote.restaurant.RestaurantResponseDto
import org.apptolast.menuadmin.domain.model.AllergenType
import org.apptolast.menuadmin.domain.model.ContainmentLevel
import org.apptolast.menuadmin.domain.model.Dish
import org.apptolast.menuadmin.domain.model.DishAllergen
import org.apptolast.menuadmin.domain.model.Ingredient
import org.apptolast.menuadmin.domain.model.Menu
import org.apptolast.menuadmin.domain.model.Restaurant
import org.apptolast.menuadmin.domain.model.SafetyLevel
import org.apptolast.menuadmin.domain.model.Section
import kotlin.time.Clock
import kotlin.time.Instant

fun RestaurantResponseDto.toDomain(): Restaurant =
    Restaurant(
        id = id,
        name = name,
        slug = slug,
        description = description,
        address = address,
        phone = phone,
        logoUrl = logoUrl,
        active = active,
    )

fun MenuResponseDto.toDomain(): Menu {
    val domainSections = sections.map { it.toDomain() }
    val allDishes = domainSections.flatMap { it.dishes }
    return Menu(
        id = id,
        name = name,
        description = description,
        displayOrder = displayOrder,
        sections = domainSections,
        archived = archived,
        dishes = allDishes,
    )
}

fun SectionResponseDto.toDomain(): Section =
    Section(
        id = id,
        name = name,
        displayOrder = displayOrder,
    )

fun DishResponseDto.toDomain(): Dish {
    val domainAllergens = allergens.map { it.toDomain() }
    return Dish(
        id = id,
        name = name,
        description = description,
        price = price,
        sectionId = sectionId ?: "",
        imageUrl = imageUrl,
        isAvailable = available,
        dishAllergens = domainAllergens,
        allergens = domainAllergens.mapNotNull { it.allergenType }.toSet(),
        safetyLevel = safetyLevel?.let { SafetyLevel.fromApi(it) },
        matchedAllergens = matchedAllergens,
    )
}

fun DishAllergenResponseDto.toDomain(): DishAllergen =
    DishAllergen(
        allergenId = allergenId,
        code = code,
        allergenType = AllergenType.fromApiCode(code),
        containmentLevel = ContainmentLevel.fromApi(containmentLevel),
        notes = notes,
    )

@OptIn(kotlin.time.ExperimentalTime::class)
fun IngredientResponseDto.toDomain(): Ingredient =
    Ingredient(
        id = id,
        name = name,
        brand = brand,
        supplier = supplier,
        allergens = allergens.mapNotNull { AllergenType.fromApiCode(it) }.toSet(),
        traces = traces.mapNotNull { AllergenType.fromApiCode(it) }.toSet(),
        ocrRawText = ocrRawText ?: "",
        notes = notes ?: "",
        createdAt = createdAt?.let { runCatching { Instant.parse(it) }.getOrNull() }
            ?: Clock.System.now(),
        updatedAt = updatedAt?.let { runCatching { Instant.parse(it) }.getOrNull() }
            ?: Clock.System.now(),
    )
