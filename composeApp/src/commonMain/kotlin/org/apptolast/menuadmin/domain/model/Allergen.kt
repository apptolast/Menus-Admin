package org.apptolast.menuadmin.domain.model

import androidx.compose.ui.graphics.Color

enum class AllergenType(
    val id: Int,
    val jsonKey: String,
    val nameEs: String,
    val nameEn: String,
    val icon: Char,
    val color: Color,
) {
    GLUTEN(1, "gluten", "Gluten", "Gluten", '\uE39E', Color(0xFFF59E0B)),
    CRUSTACEANS(2, "crustaceans", "Crustáceos", "Crustaceans", '\uE4F7', Color(0xFFEF4444)),
    EGGS(3, "eggs", "Huevos", "Eggs", '\uE25D', Color(0xFFF97316)),
    FISH(4, "fish", "Pescado", "Fish", '\uE3A6', Color(0xFF3B82F6)),
    PEANUTS(5, "peanuts", "Cacahuetes", "Peanuts", '\uE39B', Color(0xFF92400E)),
    SOY(6, "soy", "Soja", "Soy", '\uE38F', Color(0xFF84CC16)),
    DAIRY(7, "milk", "Lácteos", "Dairy", '\uE399', Color(0xFF60A5FA)),
    TREE_NUTS(8, "nuts", "Frutos Secos", "Tree Nuts", '\uE2F5', Color(0xFF78350F)),
    CELERY(9, "celery", "Apio", "Celery", '\uE2DE', Color(0xFF22C55E)),
    MUSTARD(10, "mustard", "Mostaza", "Mustard", '\uE0D6', Color(0xFFEAB308)),
    SESAME(11, "sesame", "Sésamo", "Sesame", '\uE345', Color(0xFF9CA3AF)),
    SULFITES(12, "sulphites", "Sulfitos", "Sulfites", '\uE0D5', Color(0xFF8B5CF6)),
    LUPINS(13, "lupins", "Altramuces", "Lupins", '\uE2D4', Color(0xFFEC4899)),
    MOLLUSKS(14, "molluscs", "Moluscos", "Mollusks", '\uE4F8', Color(0xFF14B8A6)),
    ;

    companion object {
        private val byJsonKey: Map<String, AllergenType> = entries.associateBy { it.jsonKey }

        fun fromJsonKey(key: String): AllergenType? = byJsonKey[key.lowercase()]
    }
}
