package org.apptolast.menuadmin.presentation.screens.recipes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import org.apptolast.menuadmin.domain.model.AllergenType
import org.apptolast.menuadmin.domain.model.DishCategory
import org.apptolast.menuadmin.domain.model.Ingredient
import org.apptolast.menuadmin.domain.model.Recipe
import org.apptolast.menuadmin.domain.model.RecipeIngredient
import org.apptolast.menuadmin.presentation.components.AllergenBadge
import org.apptolast.menuadmin.presentation.theme.BgCard
import org.apptolast.menuadmin.presentation.theme.Blue500
import org.apptolast.menuadmin.presentation.theme.BorderLight
import org.apptolast.menuadmin.presentation.theme.Green500
import org.apptolast.menuadmin.presentation.theme.MenuAdminTheme
import org.apptolast.menuadmin.presentation.theme.TextPrimary
import org.apptolast.menuadmin.presentation.theme.TextSecondary
import org.apptolast.menuadmin.presentation.theme.TextWhite
import kotlin.time.Clock

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecipeCard(
    recipe: Recipe,
    allIngredients: List<Ingredient>,
    onToggleActive: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Aggregate allergens from all ingredients in the recipe
    val aggregatedAllergens: Set<AllergenType> = recipe.ingredients
        .mapNotNull { recipeIngredient ->
            allIngredients.find { it.id == recipeIngredient.ingredientId }
        }
        .flatMap { it.allergens }
        .toSet()

    // Build compact ingredient text
    val ingredientText = recipe.ingredients.joinToString(", ") { ingredient ->
        val qty = if (ingredient.quantity > 0) "${ingredient.quantity} ${ingredient.unit} " else ""
        "$qty${ingredient.ingredientName}"
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(width = 1.dp, color = BorderLight, shape = RoundedCornerShape(12.dp))
            .background(BgCard)
            .clickable(onClick = onClick)
            .padding(16.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            // Name + Active Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = recipe.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Switch(
                    checked = recipe.isActive,
                    onCheckedChange = { onToggleActive() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = TextWhite,
                        checkedTrackColor = Green500,
                        uncheckedThumbColor = TextWhite,
                        uncheckedTrackColor = BorderLight,
                    ),
                )
            }

            // Category Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Blue500.copy(alpha = 0.1f))
                    .padding(horizontal = 10.dp, vertical = 4.dp),
            ) {
                Text(
                    text = recipe.category.labelEs,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Blue500,
                )
            }

            // Allergen Badges
            if (aggregatedAllergens.isNotEmpty()) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    aggregatedAllergens.forEach { allergen ->
                        AllergenBadge(
                            allergenType = allergen,
                            isActive = true,
                        )
                    }
                }
            }

            // Ingredient list (compact)
            if (ingredientText.isNotBlank()) {
                Text(
                    text = ingredientText,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            // Ingredient count
            Text(
                text = "${recipe.ingredients.size} ingredientes",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = TextSecondary,
            )
        }
    }
}

@Preview
@Composable
private fun RecipeCardPreview() {
    MenuAdminTheme {
        RecipeCard(
            recipe = Recipe(
                id = "rec-1",
                name = "Croquetas Ibericas del Puchero",
                category = DishCategory.ENTRANTE,
                ingredients = listOf(
                    RecipeIngredient("ing-1", "Harina de trigo", 200.0, "g"),
                    RecipeIngredient("ing-2", "Leche entera", 500.0, "ml"),
                ),
                isActive = true,
                createdAt = Clock.System.now(),
                updatedAt = Clock.System.now(),
            ),
            allIngredients = listOf(
                Ingredient(
                    id = "ing-1",
                    name = "Harina de trigo",
                    allergens = setOf(AllergenType.GLUTEN),
                    createdAt = Clock.System.now(),
                    updatedAt = Clock.System.now(),
                ),
                Ingredient(
                    id = "ing-2",
                    name = "Leche entera",
                    allergens = setOf(AllergenType.DAIRY),
                    createdAt = Clock.System.now(),
                    updatedAt = Clock.System.now(),
                ),
            ),
            onToggleActive = {},
            onClick = {},
        )
    }
}
