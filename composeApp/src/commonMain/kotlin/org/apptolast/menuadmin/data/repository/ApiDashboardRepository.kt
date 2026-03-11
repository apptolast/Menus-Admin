package org.apptolast.menuadmin.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.apptolast.menuadmin.domain.model.AllergenType
import org.apptolast.menuadmin.domain.model.DashboardStats
import org.apptolast.menuadmin.domain.repository.DashboardRepository
import org.apptolast.menuadmin.domain.repository.DishRepository
import org.apptolast.menuadmin.domain.repository.MenuRepository

class ApiDashboardRepository(
    private val menuRepository: MenuRepository,
    private val dishRepository: DishRepository,
) : DashboardRepository {
    override fun getDashboardStats(): Flow<DashboardStats> {
        return combine(
            menuRepository.getAllMenus(),
            dishRepository.getAllDishes(),
        ) { menus, dishes ->
            val totalSections = menus.sumOf { it.sections.size }

            // Count allergen frequency across all dishes
            val allergenFrequency = mutableMapOf<AllergenType, Int>()
            for (dish in dishes) {
                for (allergen in dish.allergens) {
                    allergenFrequency[allergen] = (allergenFrequency[allergen] ?: 0) + 1
                }
            }

            DashboardStats(
                totalIngredients = dishes.size,
                activeRecipes = totalSections,
                totalMenus = menus.size,
                associatedCompanies = 1,
                recentActivity = emptyList(),
                allergenFrequency = allergenFrequency.toMap(),
            )
        }
    }
}
