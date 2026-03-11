package org.apptolast.menuadmin.di

import kotlinx.serialization.json.Json
import org.apptolast.menuadmin.data.repository.MockDashboardRepository
import org.apptolast.menuadmin.data.repository.MockIngredientRepository
import org.apptolast.menuadmin.data.repository.MockMenuRepository
import org.apptolast.menuadmin.data.repository.MockRecipeRepository
import org.apptolast.menuadmin.domain.repository.DashboardRepository
import org.apptolast.menuadmin.domain.repository.IngredientRepository
import org.apptolast.menuadmin.domain.repository.MenuRepository
import org.apptolast.menuadmin.domain.repository.RecipeRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            prettyPrint = true
        }
    }

    singleOf(::MockIngredientRepository) bind IngredientRepository::class
    singleOf(::MockRecipeRepository) bind RecipeRepository::class
    singleOf(::MockMenuRepository) bind MenuRepository::class
    singleOf(::MockDashboardRepository) bind DashboardRepository::class
}
