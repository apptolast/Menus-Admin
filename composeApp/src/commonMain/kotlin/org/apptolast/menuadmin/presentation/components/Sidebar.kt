package org.apptolast.menuadmin.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.apptolast.menuadmin.domain.model.Restaurant
import org.apptolast.menuadmin.navigation.BackupRestoreRoute
import org.apptolast.menuadmin.navigation.CartaDigitalRoute
import org.apptolast.menuadmin.navigation.DashboardRoute
import org.apptolast.menuadmin.navigation.IngredientsRoute
import org.apptolast.menuadmin.navigation.MenusRoute
import org.apptolast.menuadmin.navigation.ProfileRoute
import org.apptolast.menuadmin.navigation.RecipesRoute
import org.apptolast.menuadmin.navigation.RestaurantsRoute
import org.apptolast.menuadmin.navigation.SettingsRoute
import org.apptolast.menuadmin.presentation.theme.Blue500
import org.apptolast.menuadmin.presentation.theme.MenuAdminTheme
import org.apptolast.menuadmin.presentation.theme.SidebarDark
import org.apptolast.menuadmin.presentation.theme.SidebarDarkSurface
import org.apptolast.menuadmin.presentation.theme.TextMuted
import org.apptolast.menuadmin.presentation.theme.TextWhite

@Composable
fun Sidebar(
    currentRoute: String?,
    selectedRestaurant: Restaurant?,
    onNavigate: (Any) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .width(260.dp)
            .fillMaxHeight()
            .background(SidebarDark)
            .padding(vertical = 24.dp, horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        // Logo area
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(horizontal = 12.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.Restaurant,
                contentDescription = "AllergenGuard",
                tint = Blue500,
                modifier = Modifier.size(28.dp),
            )
            Text(
                text = "AllergenGuard",
                color = TextWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Blue500)
                    .padding(horizontal = 6.dp, vertical = 2.dp),
            ) {
                Text(
                    text = "PRO",
                    color = TextWhite,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        HorizontalDivider(color = SidebarDarkSurface)

        Spacer(modifier = Modifier.height(20.dp))

        // Section: MENU PRINCIPAL
        SectionHeader(title = "MENU PRINCIPAL")

        Spacer(modifier = Modifier.height(8.dp))

        NavItem(
            icon = Icons.Outlined.Dashboard,
            label = "Dashboard",
            isSelected = currentRoute == DashboardRoute::class.qualifiedName,
            onClick = { onNavigate(DashboardRoute) },
        )
        NavItem(
            icon = Icons.Outlined.Inventory2,
            label = "Ingredientes",
            isSelected = currentRoute == IngredientsRoute::class.qualifiedName,
            onClick = { onNavigate(IngredientsRoute) },
        )
        NavItem(
            icon = Icons.Outlined.Storefront,
            label = "Restaurantes",
            isSelected = currentRoute?.contains("RestaurantsRoute") == true ||
                currentRoute?.contains("RestaurantDetailRoute") == true,
            onClick = { onNavigate(RestaurantsRoute) },
        )

        // Conditional section: selected restaurant
        if (selectedRestaurant != null) {
            Spacer(modifier = Modifier.height(24.dp))

            SectionHeader(
                title = selectedRestaurant.name.take(25).uppercase(),
            )

            Spacer(modifier = Modifier.height(8.dp))

            NavItem(
                icon = Icons.Outlined.Restaurant,
                label = "Recetas",
                isSelected = currentRoute?.contains("RecipesRoute") == true,
                onClick = { onNavigate(RecipesRoute(selectedRestaurant.id)) },
            )
            NavItem(
                icon = Icons.Outlined.Book,
                label = "Menus",
                isSelected = currentRoute?.contains("MenusRoute") == true,
                onClick = { onNavigate(MenusRoute(selectedRestaurant.id)) },
            )
            NavItem(
                icon = Icons.Outlined.Smartphone,
                label = "Carta Digital",
                isSelected = currentRoute?.contains("CartaDigitalRoute") == true,
                onClick = { onNavigate(CartaDigitalRoute(selectedRestaurant.id)) },
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section: HERRAMIENTAS
        SectionHeader(title = "HERRAMIENTAS")

        Spacer(modifier = Modifier.height(8.dp))

        NavItem(
            icon = Icons.Outlined.Storage,
            label = "Backup / Restaurar",
            isSelected = currentRoute == BackupRestoreRoute::class.qualifiedName,
            onClick = { onNavigate(BackupRestoreRoute) },
        )
        NavItem(
            icon = Icons.Outlined.Settings,
            label = "Configuracion",
            isSelected = currentRoute == SettingsRoute::class.qualifiedName,
            onClick = { onNavigate(SettingsRoute) },
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Section: CUENTA
        SectionHeader(title = "CUENTA")

        Spacer(modifier = Modifier.height(8.dp))

        NavItem(
            icon = Icons.Outlined.Person,
            label = "Mi Perfil",
            isSelected = currentRoute == ProfileRoute::class.qualifiedName,
            onClick = { onNavigate(ProfileRoute) },
        )

        Spacer(modifier = Modifier.weight(1f))

        HorizontalDivider(color = SidebarDarkSurface)

        Spacer(modifier = Modifier.height(8.dp))

        NavItem(
            icon = Icons.AutoMirrored.Outlined.Logout,
            label = "Cerrar sesion",
            isSelected = false,
            onClick = onLogout,
        )
    }
}

@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        color = TextMuted,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 1.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
    )
}

@Preview
@Composable
private fun SidebarPreview() {
    MenuAdminTheme {
        Sidebar(
            currentRoute = null,
            selectedRestaurant = Restaurant(
                id = "1",
                name = "Hotel Palace Barcelona",
                slug = "hotel-palace-barcelona",
                active = true,
            ),
            onNavigate = {},
            onLogout = {},
        )
    }
}

@Preview
@Composable
private fun SidebarNoRestaurantPreview() {
    MenuAdminTheme {
        Sidebar(
            currentRoute = null,
            selectedRestaurant = null,
            onNavigate = {},
            onLogout = {},
        )
    }
}
