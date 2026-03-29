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
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Settings
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
import org.apptolast.menuadmin.navigation.BackupRestoreRoute
import org.apptolast.menuadmin.navigation.DashboardRoute
import org.apptolast.menuadmin.navigation.IngredientsRoute
import org.apptolast.menuadmin.navigation.ProfileRoute
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
private fun PreviewSidebar() {
    MenuAdminTheme {
        Sidebar(
            currentRoute = null,
            onNavigate = {},
            onLogout = {},
        )
    }
}
