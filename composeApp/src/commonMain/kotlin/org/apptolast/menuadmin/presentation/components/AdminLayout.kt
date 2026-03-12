package org.apptolast.menuadmin.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import org.apptolast.menuadmin.navigation.BackupRestoreRoute
import org.apptolast.menuadmin.navigation.CartaDigitalRoute
import org.apptolast.menuadmin.navigation.DashboardRoute
import org.apptolast.menuadmin.navigation.IngredientsRoute
import org.apptolast.menuadmin.navigation.MenusRoute
import org.apptolast.menuadmin.navigation.ProfileRoute
import org.apptolast.menuadmin.navigation.RecipesRoute
import org.apptolast.menuadmin.navigation.RestaurantDetailRoute
import org.apptolast.menuadmin.navigation.RestaurantsRoute
import org.apptolast.menuadmin.navigation.SettingsRoute
import org.apptolast.menuadmin.presentation.SelectedRestaurantHolder
import org.apptolast.menuadmin.presentation.screens.backup.BackupRestoreScreen
import org.apptolast.menuadmin.presentation.screens.cartadigital.CartaDigitalScreen
import org.apptolast.menuadmin.presentation.screens.dashboard.DashboardScreen
import org.apptolast.menuadmin.presentation.screens.ingredients.IngredientsScreen
import org.apptolast.menuadmin.presentation.screens.menus.MenusScreen
import org.apptolast.menuadmin.presentation.screens.profile.ProfileScreen
import org.apptolast.menuadmin.presentation.screens.recipes.RecipesScreen
import org.apptolast.menuadmin.presentation.screens.restaurants.RestaurantsListScreen
import org.apptolast.menuadmin.presentation.screens.restaurants.detail.RestaurantDetailScreen
import org.apptolast.menuadmin.presentation.screens.settings.SettingsScreen
import org.apptolast.menuadmin.presentation.theme.BgSecondary
import org.koin.compose.koinInject

@Composable
fun AdminLayout(
    navController: NavHostController,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val selectedRestaurantHolder: SelectedRestaurantHolder = koinInject()
    val selectedRestaurant by selectedRestaurantHolder.selected.collectAsState()

    Row(modifier = modifier.fillMaxSize()) {
        Sidebar(
            currentRoute = currentRoute,
            selectedRestaurant = selectedRestaurant,
            onNavigate = { route ->
                navController.navigate(route) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            onLogout = onLogout,
            modifier = Modifier
                .width(260.dp)
                .fillMaxHeight(),
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(BgSecondary),
        ) {
            NavHost(
                navController = navController,
                startDestination = DashboardRoute,
            ) {
                composable<DashboardRoute> {
                    DashboardScreen()
                }
                composable<IngredientsRoute> {
                    IngredientsScreen()
                }
                composable<RestaurantsRoute> {
                    RestaurantsListScreen(
                        onNavigateToRestaurant = { restaurantId ->
                            navController.navigate(RestaurantDetailRoute(restaurantId)) {
                                launchSingleTop = true
                            }
                        },
                    )
                }
                composable<RestaurantDetailRoute> { backStackEntry ->
                    val route = backStackEntry.toRoute<RestaurantDetailRoute>()
                    RestaurantDetailScreen(
                        onBack = { navController.popBackStack() },
                        onNavigateToRecipes = { restaurantId ->
                            navController.navigate(RecipesRoute(restaurantId)) {
                                launchSingleTop = true
                            }
                        },
                        onNavigateToMenus = { restaurantId ->
                            navController.navigate(MenusRoute(restaurantId)) {
                                launchSingleTop = true
                            }
                        },
                        onNavigateToCarta = { restaurantId ->
                            navController.navigate(CartaDigitalRoute(restaurantId)) {
                                launchSingleTop = true
                            }
                        },
                    )
                }
                composable<RecipesRoute> {
                    RecipesScreen()
                }
                composable<MenusRoute> {
                    MenusScreen()
                }
                composable<CartaDigitalRoute> {
                    CartaDigitalScreen()
                }
                composable<SettingsRoute> {
                    SettingsScreen()
                }
                composable<BackupRestoreRoute> {
                    BackupRestoreScreen()
                }
                composable<ProfileRoute> {
                    ProfileScreen(onAccountDeleted = onLogout)
                }
            }
        }
    }
}
