package org.apptolast.menuadmin.presentation.screens.recipes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.apptolast.menuadmin.domain.model.AllergenType
import org.apptolast.menuadmin.domain.model.Recipe
import org.apptolast.menuadmin.presentation.components.AllergenBadge
import org.apptolast.menuadmin.presentation.theme.BgCard
import org.apptolast.menuadmin.presentation.theme.Blue500
import org.apptolast.menuadmin.presentation.theme.BorderLight
import org.apptolast.menuadmin.presentation.theme.MenuAdminTheme
import org.apptolast.menuadmin.presentation.theme.TextPrimary
import org.apptolast.menuadmin.presentation.theme.TextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecipeCard(
    recipe: Recipe,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val categoryLabel = recipe.category

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
            // Name
            Text(
                text = recipe.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            // Category Badge
            if (categoryLabel.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Blue500.copy(alpha = 0.1f))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                ) {
                    Text(
                        text = categoryLabel,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Blue500,
                    )
                }
            }

            // Allergen Badges (when full data is available)
            if (recipe.computedAllergens.isNotEmpty()) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    recipe.computedAllergens.forEach { allergen ->
                        AllergenBadge(
                            allergenType = allergen,
                            isActive = true,
                        )
                    }
                }
            } else if (recipe.allergenCount > 0) {
                // Summary data only — show count badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Blue500.copy(alpha = 0.08f))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                ) {
                    Text(
                        text = "${recipe.allergenCount} alergenos",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Blue500,
                    )
                }
            }

            // Ingredient count
            Text(
                text = "${recipe.ingredientCount} ingredientes",
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
                category = "Entrantes",
                isActive = true,
                ingredientCount = 5,
                allergenCount = 2,
                computedAllergens = setOf(AllergenType.GLUTEN, AllergenType.DAIRY),
            ),
            onClick = {},
        )
    }
}
