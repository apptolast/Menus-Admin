package org.apptolast.menuadmin.presentation.screens.cartadigital

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import org.apptolast.menuadmin.presentation.components.AllergenBadge
import org.apptolast.menuadmin.presentation.theme.Blue500
import org.apptolast.menuadmin.presentation.theme.BorderLight
import org.apptolast.menuadmin.presentation.theme.Green100
import org.apptolast.menuadmin.presentation.theme.Green500
import org.apptolast.menuadmin.presentation.theme.MenuAdminTheme
import org.apptolast.menuadmin.presentation.theme.Red100
import org.apptolast.menuadmin.presentation.theme.Red500
import org.apptolast.menuadmin.presentation.theme.TextPrimary
import org.apptolast.menuadmin.presentation.theme.TextSecondary
import org.apptolast.menuadmin.presentation.theme.TextWhite
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CartaDigitalScreen(viewModel: CartaDigitalViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    CartaDigitalContent(
        uiState = uiState,
        onSelectMenu = viewModel::onSelectMenu,
        onToggleAllergen = viewModel::onToggleAllergen,
        onClearAllergens = viewModel::onClearAllergens,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CartaDigitalContent(
    uiState: CartaDigitalUiState,
    onSelectMenu: (String) -> Unit,
    onToggleAllergen: (AllergenType) -> Unit,
    onClearAllergens: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState.isLoading) {
        CircularProgressIndicator()
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        // Header
        Column {
            Text(
                text = "Carta Digital Interactiva",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
            )
            Text(
                text = "Filtra platos segun las alergias del cliente",
                fontSize = 14.sp,
                color = TextSecondary,
            )
        }

        // Menu Selector
        if (uiState.availableMenus.size > 1) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                uiState.availableMenus.forEach { menu ->
                    FilterChip(
                        selected = menu.id == uiState.selectedMenuId,
                        onClick = { onSelectMenu(menu.id) },
                        label = {
                            Text(
                                text = menu.name,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Blue500,
                            selectedLabelColor = TextWhite,
                            labelColor = TextPrimary,
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = BorderLight,
                            selectedBorderColor = Blue500,
                            enabled = true,
                            selected = menu.id == uiState.selectedMenuId,
                        ),
                    )
                }
            }
        }

        // Allergen Selector
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "¿A que tiene alergia el cliente?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                )
                if (uiState.selectedAllergens.isNotEmpty()) {
                    TextButton(onClick = { onClearAllergens() }) {
                        Text(
                            text = "Limpiar seleccion",
                            fontSize = 13.sp,
                            color = Blue500,
                        )
                    }
                }
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AllergenType.entries.forEach { allergen ->
                    AllergenBadge(
                        allergenType = allergen,
                        isActive = allergen in uiState.selectedAllergens,
                        onClick = { onToggleAllergen(allergen) },
                    )
                }
            }
        }

        // Results counter
        if (uiState.selectedAllergens.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Green100)
                    .padding(16.dp),
            ) {
                Text(
                    text = "${uiState.safeDishes.size} Platos Seguros de ${uiState.allDishes.size} total",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Green500,
                )
            }
        }

        // Safe Dishes
        if (uiState.selectedAllergens.isNotEmpty() && uiState.safeDishes.isNotEmpty()) {
            DishSection(
                title = "Platos Seguros",
                titleColor = Green500,
                borderColor = Green500,
                bgColor = Green100,
                dishes = uiState.safeDishes,
                userAllergens = uiState.selectedAllergens,
            )
        }

        // Unsafe Dishes
        if (uiState.selectedAllergens.isNotEmpty() && uiState.unsafeDishes.isNotEmpty()) {
            DishSection(
                title = "Platos con Alergenos",
                titleColor = Red500,
                borderColor = Red500,
                bgColor = Red100,
                dishes = uiState.unsafeDishes,
                userAllergens = uiState.selectedAllergens,
            )
        }

        // Show all dishes when no allergens selected
        if (uiState.selectedAllergens.isEmpty() && uiState.allDishes.isNotEmpty()) {
            Text(
                text = "Todos los platos (${uiState.allDishes.size})",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
            )
            uiState.allDishes.forEach { dish ->
                DishCard(
                    dish = dish,
                    userAllergens = emptySet(),
                    accentColor = Blue500,
                )
            }
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
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DishSection(
    title: String,
    titleColor: androidx.compose.ui.graphics.Color,
    borderColor: androidx.compose.ui.graphics.Color,
    bgColor: androidx.compose.ui.graphics.Color,
    dishes: List<Dish>,
    userAllergens: Set<AllergenType>,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "$title (${dishes.size})",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = titleColor,
        )
        dishes.forEach { dish ->
            DishCard(
                dish = dish,
                userAllergens = userAllergens,
                accentColor = borderColor,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DishCard(
    dish: Dish,
    userAllergens: Set<AllergenType>,
    accentColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(width = 1.dp, color = BorderLight, shape = RoundedCornerShape(8.dp))
            .background(androidx.compose.ui.graphics.Color.White),
    ) {
        // Left accent bar
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(120.dp)
                .background(accentColor),
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = dish.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                    )
                    Text(
                        text = dish.category.labelEs,
                        fontSize = 12.sp,
                        color = TextSecondary,
                    )
                }
                Text(
                    text = "${dish.price.toInt()}.${((dish.price * 100).toInt() % 100).toString().padStart(2, '0')}€",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                )
            }

            if (dish.description.isNotBlank()) {
                Text(
                    text = dish.description,
                    fontSize = 13.sp,
                    color = TextSecondary,
                    maxLines = 2,
                )
            }

            if (dish.allergens.isNotEmpty()) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    dish.allergens.forEach { allergen ->
                        val isConflict = allergen in userAllergens
                        AllergenBadge(
                            allergenType = allergen,
                            isActive = isConflict,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CartaDigitalContentPreview() {
    MenuAdminTheme {
        CartaDigitalContent(
            uiState = CartaDigitalUiState(
                isLoading = false,
                selectedAllergens = setOf(AllergenType.GLUTEN),
                allDishes = listOf(
                    Dish(
                        id = "d1",
                        name = "Butifarra a la Brasa",
                        description = "Butifarra artesana",
                        price = 14.50,
                        category = DishCategory.COMBINADOS,
                        allergens = emptySet(),
                    ),
                    Dish(
                        id = "d2",
                        name = "Croquetas Ibericas",
                        description = "Croquetas cremosas",
                        price = 12.50,
                        category = DishCategory.ENTRANTE,
                        allergens = setOf(AllergenType.GLUTEN, AllergenType.DAIRY),
                    ),
                ),
                safeDishes = listOf(
                    Dish(
                        id = "d1",
                        name = "Butifarra a la Brasa",
                        description = "Butifarra artesana",
                        price = 14.50,
                        category = DishCategory.COMBINADOS,
                        allergens = emptySet(),
                    ),
                ),
                unsafeDishes = listOf(
                    Dish(
                        id = "d2",
                        name = "Croquetas Ibericas",
                        description = "Croquetas cremosas",
                        price = 12.50,
                        category = DishCategory.ENTRANTE,
                        allergens = setOf(AllergenType.GLUTEN, AllergenType.DAIRY),
                    ),
                ),
            ),
            onSelectMenu = {},
            onToggleAllergen = {},
            onClearAllergens = {},
        )
    }
}
