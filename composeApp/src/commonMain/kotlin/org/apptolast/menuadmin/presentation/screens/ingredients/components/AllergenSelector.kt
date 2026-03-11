package org.apptolast.menuadmin.presentation.screens.ingredients.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.apptolast.menuadmin.domain.model.AllergenType
import org.apptolast.menuadmin.presentation.components.LucideIcon
import org.apptolast.menuadmin.presentation.theme.Blue100
import org.apptolast.menuadmin.presentation.theme.BorderLight
import org.apptolast.menuadmin.presentation.theme.MenuAdminTheme
import org.apptolast.menuadmin.presentation.theme.TextPrimary
import org.apptolast.menuadmin.presentation.theme.TextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AllergenSelector(
    selectedAllergens: Set<AllergenType>,
    onToggle: (AllergenType) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AllergenType.entries.forEach { allergen ->
            AllergenCard(
                allergenType = allergen,
                isActive = allergen in selectedAllergens,
                onClick = { onToggle(allergen) },
            )
        }
    }
}

@Composable
private fun AllergenCard(
    allergenType: AllergenType,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (isActive) {
        allergenType.color.copy(alpha = 0.12f)
    } else {
        Blue100.copy(alpha = 0.3f)
    }
    val borderColor = if (isActive) allergenType.color else BorderLight
    val textColor = if (isActive) allergenType.color else TextSecondary
    val shape = RoundedCornerShape(12.dp)

    Column(
        modifier = modifier
            .widthIn(min = 100.dp)
            .clip(shape)
            .border(
                width = if (isActive) 2.dp else 1.dp,
                color = borderColor,
                shape = shape,
            )
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        LucideIcon(
            codepoint = allergenType.icon,
            size = 28.sp,
            color = if (isActive) allergenType.color else TextSecondary.copy(alpha = 0.5f),
        )
        Text(
            text = allergenType.nameEs,
            color = if (isActive) TextPrimary else textColor,
            fontSize = 12.sp,
            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}

@Preview
@Composable
private fun PreviewAllergenSelector() {
    MenuAdminTheme {
        AllergenSelector(
            selectedAllergens = setOf(AllergenType.GLUTEN, AllergenType.DAIRY),
            onToggle = {},
        )
    }
}

@Preview
@Composable
private fun PreviewAllergenCardActive() {
    MenuAdminTheme {
        AllergenCard(
            allergenType = AllergenType.GLUTEN,
            isActive = true,
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun PreviewAllergenCardInactive() {
    MenuAdminTheme {
        AllergenCard(
            allergenType = AllergenType.DAIRY,
            isActive = false,
            onClick = {},
        )
    }
}
