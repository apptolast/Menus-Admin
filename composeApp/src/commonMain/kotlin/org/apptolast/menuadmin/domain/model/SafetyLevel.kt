package org.apptolast.menuadmin.domain.model

import androidx.compose.ui.graphics.Color

enum class SafetyLevel(
    val apiValue: String,
    val labelEs: String,
    val color: Color,
) {
    SAFE("SAFE", "Seguro", Color(0xFF22C55E)),
    RISK("RISK", "Riesgo", Color(0xFFF59E0B)),
    DANGER("DANGER", "Peligro", Color(0xFFEF4444)),
    ;

    companion object {
        fun fromApi(value: String): SafetyLevel? = entries.firstOrNull { it.apiValue.equals(value, ignoreCase = true) }
    }
}
