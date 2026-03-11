package org.apptolast.menuadmin.data.remote

object ApiConstants {
    const val BASE_URL = "https://menus-api-dev.apptolast.com"

    // Auth
    const val AUTH_LOGIN = "/api/v1/auth/login"
    const val AUTH_REGISTER = "/api/v1/auth/register"
    const val AUTH_REFRESH = "/api/v1/auth/refresh"
    const val AUTH_GOOGLE_CALLBACK = "/api/v1/auth/oauth2/google/callback"
    const val AUTH_CONSENT = "/api/v1/auth/consent"

    // Admin - Restaurant
    const val ADMIN_RESTAURANT = "/api/v1/admin/restaurant"

    // Admin - Menus
    const val ADMIN_MENUS = "/api/v1/admin/menus"

    // Admin - Dishes
    const val ADMIN_DISHES = "/api/v1/admin/dishes"

    // Public
    const val ALLERGENS = "/api/v1/allergens"
    const val RESTAURANTS = "/api/v1/restaurants"

    // User
    const val USER_ALLERGEN_PROFILE = "/api/v1/users/me/allergen-profile"
    const val USER_DATA_EXPORT = "/api/v1/users/me/data-export"
    const val USER_DATA_DELETE = "/api/v1/users/me/data-delete"
}
