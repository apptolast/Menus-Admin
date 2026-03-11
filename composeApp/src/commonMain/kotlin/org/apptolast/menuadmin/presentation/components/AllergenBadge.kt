package org.apptolast.menuadmin.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.apptolast.menuadmin.domain.model.AllergenType
import org.apptolast.menuadmin.presentation.theme.AllergenInactiveBg
import org.apptolast.menuadmin.presentation.theme.AllergenInactiveText
import org.apptolast.menuadmin.presentation.theme.BorderLight
import org.apptolast.menuadmin.presentation.theme.MenuAdminTheme

@Composable
fun AllergenBadge(
    allergenType: AllergenType,
    isActive: Boolean,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (isActive) {
        allergenType.color.copy(alpha = 0.15f)
    } else {
        AllergenInactiveBg
    }
    val contentColor = if (isActive) allergenType.color else AllergenInactiveText
    val borderColor = if (isActive) allergenType.color else BorderLight

    val shape = RoundedCornerShape(12.dp)

    Row(
        modifier = modifier
            .clip(shape)
            .border(width = 1.dp, color = borderColor, shape = shape)
            .background(backgroundColor)
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                },
            )
            .padding(vertical = 6.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        LucideIcon(
            codepoint = allergenType.icon,
            size = 14.sp,
            color = contentColor,
        )
        Text(
            text = allergenType.nameEs,
            color = contentColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Preview
@Composable
private fun AllergenBadgeActivePreview() {
    MenuAdminTheme {
        AllergenBadge(
            allergenType = AllergenType.GLUTEN,
            isActive = true,
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun AllergenBadgeInactivePreview() {
    MenuAdminTheme {
        AllergenBadge(
            allergenType = AllergenType.DAIRY,
            isActive = false,
            onClick = {},
        )
    }
}
