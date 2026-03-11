package org.apptolast.menuadmin

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import org.apptolast.menuadmin.presentation.components.AdminLayout
import org.apptolast.menuadmin.presentation.theme.MenuAdminTheme

@Composable
fun App() {
    MenuAdminTheme {
        val navController = rememberNavController()
        AdminLayout(navController = navController)
    }
}
