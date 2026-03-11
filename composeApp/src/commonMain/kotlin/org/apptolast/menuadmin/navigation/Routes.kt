package org.apptolast.menuadmin.navigation

import kotlinx.serialization.Serializable

@Serializable
object LoginRoute

@Serializable
object DashboardRoute

@Serializable
object IngredientsRoute

@Serializable
object RecipesRoute

@Serializable
object MenusRoute

@Serializable
data class AllergenMatrixRoute(
    val menuId: String,
)

@Serializable
object CartaDigitalRoute

@Serializable
object SettingsRoute

@Serializable
object BackupRestoreRoute

@Serializable
object ProfileRoute
