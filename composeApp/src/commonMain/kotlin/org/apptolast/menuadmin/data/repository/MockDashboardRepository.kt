package org.apptolast.menuadmin.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.apptolast.menuadmin.data.local.MockDataProvider
import org.apptolast.menuadmin.domain.model.AllergenType
import org.apptolast.menuadmin.domain.model.DashboardStats
import org.apptolast.menuadmin.domain.repository.DashboardRepository
import org.apptolast.menuadmin.domain.repository.IngredientRepository
import org.apptolast.menuadmin.domain.repository.MenuRepository
import org.apptolast.menuadmin.domain.repository.RecipeRepository

class MockDashboardRepository(
    private val ingredientRepository: IngredientRepository,
    private val recipeRepository: RecipeRepository,
    private val menuRepository: MenuRepository,
) : DashboardRepository {
    override fun getDashboardStats(): Flow<DashboardStats> {
        return combine(
            ingredientRepository.getAllIngredients(),
            recipeRepository.getAllRecipes(),
            menuRepository.getAllMenus(),
        ) { ingredients, recipes, menus ->

            // Count allergen frequency across all ingredients
            val allergenFrequency = mutableMapOf<AllergenType, Int>()
            for (ingredient in ingredients) {
                for (allergen in ingredient.allergens) {
                    allergenFrequency[allergen] = (allergenFrequency[allergen] ?: 0) + 1
                }
            }

            // Count distinct company names as restaurants
            val totalRestaurants = menus.map { it.companyName }.distinct().size

            DashboardStats(
                totalIngredients = ingredients.size,
                activeRecipes = recipes.count { it.isActive },
                totalMenus = menus.size,
                totalRestaurants = totalRestaurants,
                recentActivity = MockDataProvider.recentActivity.sortedByDescending { it.timestamp },
                allergenFrequency = allergenFrequency.toMap(),
            )
        }
    }
}
