package org.apptolast.menuadmin.di

import kotlinx.serialization.json.Json
import org.apptolast.menuadmin.data.remote.allergen.AllergenService
import org.apptolast.menuadmin.data.remote.auth.AuthService
import org.apptolast.menuadmin.data.remote.auth.TokenManager
import org.apptolast.menuadmin.data.remote.consumer.ConsumerService
import org.apptolast.menuadmin.data.remote.createHttpClient
import org.apptolast.menuadmin.data.remote.dish.DishService
import org.apptolast.menuadmin.data.remote.ingredient.IngredientService
import org.apptolast.menuadmin.data.remote.menu.MenuService
import org.apptolast.menuadmin.data.remote.restaurant.RestaurantService
import org.apptolast.menuadmin.data.repository.ApiDashboardRepository
import org.apptolast.menuadmin.data.repository.MockRecipeRepository
import org.apptolast.menuadmin.data.repository.RemoteAuthRepository
import org.apptolast.menuadmin.data.repository.RemoteDishRepository
import org.apptolast.menuadmin.data.repository.RemoteIngredientRepository
import org.apptolast.menuadmin.data.repository.RemoteMenuRepository
import org.apptolast.menuadmin.data.repository.RemoteRestaurantRepository
import org.apptolast.menuadmin.domain.repository.AuthRepository
import org.apptolast.menuadmin.domain.repository.DashboardRepository
import org.apptolast.menuadmin.domain.repository.DishRepository
import org.apptolast.menuadmin.domain.repository.IngredientRepository
import org.apptolast.menuadmin.domain.repository.MenuRepository
import org.apptolast.menuadmin.domain.repository.RecipeRepository
import org.apptolast.menuadmin.domain.repository.RestaurantRepository
import org.apptolast.menuadmin.presentation.SelectedRestaurantHolder
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    // Shared Json instance
    single {
        Json {
            ignoreUnknownKeys = true
            prettyPrint = true
        }
    }

    // Network infrastructure
    single { TokenManager() }
    single { createHttpClient(get(), get()) }

    // API Services
    singleOf(::AuthService)
    singleOf(::RestaurantService)
    singleOf(::MenuService)
    singleOf(::DishService)
    singleOf(::IngredientService)
    singleOf(::AllergenService)
    singleOf(::ConsumerService)

    // Real repositories (API-backed)
    singleOf(::RemoteAuthRepository) bind AuthRepository::class
    singleOf(::RemoteRestaurantRepository) bind RestaurantRepository::class
    singleOf(::RemoteDishRepository) bind DishRepository::class
    singleOf(::RemoteMenuRepository) bind MenuRepository::class
    singleOf(::RemoteIngredientRepository) bind IngredientRepository::class
    singleOf(::ApiDashboardRepository) bind DashboardRepository::class

    // Mock-only repositories (no API endpoints yet)
    singleOf(::MockRecipeRepository) bind RecipeRepository::class

    // Shared state holders
    single { SelectedRestaurantHolder() }
}
