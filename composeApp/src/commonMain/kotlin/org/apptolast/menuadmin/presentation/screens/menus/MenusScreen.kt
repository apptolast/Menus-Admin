package org.apptolast.menuadmin.presentation.screens.menus

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.PictureAsPdf
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.apptolast.menuadmin.domain.model.AllergenType
import org.apptolast.menuadmin.domain.model.Dish
import org.apptolast.menuadmin.domain.model.DishCategory
import org.apptolast.menuadmin.domain.model.Menu
import org.apptolast.menuadmin.presentation.screens.menus.components.AllergenMatrixTable
import org.apptolast.menuadmin.presentation.screens.menus.components.AllergenTableLegend
import org.apptolast.menuadmin.presentation.screens.menus.components.CategoryFilterRow
import org.apptolast.menuadmin.presentation.theme.BgCard
import org.apptolast.menuadmin.presentation.theme.Blue500
import org.apptolast.menuadmin.presentation.theme.BorderLight
import org.apptolast.menuadmin.presentation.theme.Green500
import org.apptolast.menuadmin.presentation.theme.MenuAdminTheme
import org.apptolast.menuadmin.presentation.theme.TextPrimary
import org.apptolast.menuadmin.presentation.theme.TextSecondary
import org.apptolast.menuadmin.presentation.theme.TextWhite
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock

@Composable
fun MenusScreen(viewModel: MenusViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    MenusContent(
        uiState = uiState,
        onSelectMenu = viewModel::selectMenu,
        onBack = viewModel::clearMenuSelection,
        onFilterCategory = viewModel::filterByCategory,
        onExportPdf = viewModel::exportPdf,
    )
}

@Composable
fun MenusContent(
    uiState: MenusUiState,
    onSelectMenu: (Menu) -> Unit,
    onBack: () -> Unit,
    onFilterCategory: (DishCategory?) -> Unit,
    onExportPdf: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState.isLoading) {
        CircularProgressIndicator()
        return
    }

    val selectedMenu = uiState.selectedMenu

    if (selectedMenu == null) {
        MenuListView(
            uiState = uiState,
            onSelectMenu = onSelectMenu,
        )
    } else {
        AllergenMatrixView(
            uiState = uiState,
            onBack = onBack,
            onFilterCategory = onFilterCategory,
            onExportPdf = onExportPdf,
        )
    }

    // Error display
    uiState.error?.let { error ->
        Text(
            text = error,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun MenuListView(
    uiState: MenusUiState,
    onSelectMenu: (Menu) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = "Menus",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                )
                Text(
                    text = "Gestiona los menus y sus alergenos",
                    fontSize = 14.sp,
                    color = TextSecondary,
                )
            }
            Button(
                onClick = { /* New menu action */ },
                colors = ButtonDefaults.buttonColors(containerColor = Blue500),
                shape = RoundedCornerShape(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Nuevo",
                    tint = TextWhite,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Nuevo Menu",
                    color = TextWhite,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }

        // Menu Cards
        uiState.menus.forEach { menu ->
            MenuCard(
                menu = menu,
                onClick = { onSelectMenu(menu) },
            )
        }
    }
}

@Composable
private fun MenuCard(
    menu: Menu,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(width = 1.dp, color = BorderLight, shape = RoundedCornerShape(12.dp))
            .background(BgCard)
            .clickable(onClick = onClick)
            .padding(20.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = menu.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
            )
            Text(
                text = menu.companyName,
                fontSize = 14.sp,
                color = TextSecondary,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Green500.copy(alpha = 0.1f))
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                ) {
                    Text(
                        text = "${menu.dishes.size} platos",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Green500,
                    )
                }
            }
        }
    }
}

@Composable
private fun AllergenMatrixView(
    uiState: MenusUiState,
    onBack: () -> Unit,
    onFilterCategory: (DishCategory?) -> Unit,
    onExportPdf: () -> Unit,
) {
    val menu = uiState.selectedMenu ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Header with back button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Volver",
                    tint = TextPrimary,
                )
            }
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = menu.companyName,
                    fontSize = 13.sp,
                    color = TextSecondary,
                )
                Text(
                    text = "Menu de Alergenos",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                )
            }
            OutlinedButton(
                onClick = onExportPdf,
                shape = RoundedCornerShape(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.PictureAsPdf,
                    contentDescription = "Exportar PDF",
                    modifier = Modifier.size(16.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Exportar PDF")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { /* New menu */ },
                colors = ButtonDefaults.buttonColors(containerColor = Blue500),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text("Nuevo Menu", color = TextWhite)
            }
        }

        // Description section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Blue500.copy(alpha = 0.05f))
                .border(width = 1.dp, color = Blue500.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp))
                .padding(16.dp),
        ) {
            Text(
                text = "Generar Menu Adaptado (Libre de...): Selecciona una categoria y consulta la tabla de alergenos para cada plato.",
                fontSize = 14.sp,
                color = TextSecondary,
            )
        }

        // Category Filter
        CategoryFilterRow(
            selectedCategory = uiState.selectedCategory,
            onCategorySelected = onFilterCategory,
        )

        // Allergen Matrix Table
        AllergenMatrixTable(
            dishes = menu.dishes,
            selectedCategory = uiState.selectedCategory,
        )

        // Legend footer
        AllergenTableLegend()
    }
}

@Preview
@Composable
private fun MenusContentPreview() {
    MenuAdminTheme {
        MenusContent(
            uiState = MenusUiState(
                isLoading = false,
                menus = listOf(
                    Menu(
                        id = "menu-1",
                        name = "Menu Primavera 2025",
                        companyName = "Hotel Palace Barcelona",
                        dishes = listOf(
                            Dish(
                                id = "d1",
                                name = "Croquetas Ibericas",
                                price = 12.50,
                                category = DishCategory.ENTRANTE,
                                allergens = setOf(AllergenType.GLUTEN, AllergenType.DAIRY),
                            ),
                        ),
                        createdAt = Clock.System.now(),
                        updatedAt = Clock.System.now(),
                    ),
                ),
            ),
            onSelectMenu = {},
            onBack = {},
            onFilterCategory = {},
            onExportPdf = {},
        )
    }
}
