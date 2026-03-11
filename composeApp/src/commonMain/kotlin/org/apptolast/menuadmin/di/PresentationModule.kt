package org.apptolast.menuadmin.di

import org.apptolast.menuadmin.presentation.screens.auth.AuthViewModel
import org.apptolast.menuadmin.presentation.screens.backup.BackupViewModel
import org.apptolast.menuadmin.presentation.screens.cartadigital.CartaDigitalViewModel
import org.apptolast.menuadmin.presentation.screens.dashboard.DashboardViewModel
import org.apptolast.menuadmin.presentation.screens.ingredients.IngredientsViewModel
import org.apptolast.menuadmin.presentation.screens.menus.MenusViewModel
import org.apptolast.menuadmin.presentation.screens.profile.ProfileViewModel
import org.apptolast.menuadmin.presentation.screens.recipes.RecipesViewModel
import org.apptolast.menuadmin.presentation.screens.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::AuthViewModel)
    viewModelOf(::DashboardViewModel)
    viewModelOf(::IngredientsViewModel)
    viewModelOf(::RecipesViewModel)
    viewModelOf(::MenusViewModel)
    viewModelOf(::CartaDigitalViewModel)
    viewModelOf(::BackupViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::ProfileViewModel)
}
