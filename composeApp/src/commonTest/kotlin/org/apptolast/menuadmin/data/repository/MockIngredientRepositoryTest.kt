package org.apptolast.menuadmin.data.repository

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.apptolast.menuadmin.domain.model.AllergenType
import org.apptolast.menuadmin.domain.model.Ingredient
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Clock

class MockIngredientRepositoryTest {
    private val now = Clock.System.now()

    private fun createRepository() = MockIngredientRepository()

    @Test
    fun getAllIngredients_returnsInitialMockData() =
        runTest {
            val repository = createRepository()

            val ingredients = repository.getAllIngredients().first()

            assertTrue(ingredients.isNotEmpty())
        }

    @Test
    fun addIngredient_addsToTheList() =
        runTest {
            val repository = createRepository()
            val initialCount = repository.getAllIngredients().first().size

            val newIngredient = Ingredient(
                id = "ing-new",
                name = "Pimienta negra",
                description = "Pimienta negra molida",
                brand = "Especias del Sur",
                allergens = emptySet(),
                labelInfo = "",
                createdAt = now,
                updatedAt = now,
            )

            repository.addIngredient(newIngredient)

            val updatedList = repository.getAllIngredients().first()
            assertEquals(initialCount + 1, updatedList.size)
            assertTrue(updatedList.any { it.id == "ing-new" })
        }

    @Test
    fun updateIngredient_modifiesExistingIngredient() =
        runTest {
            val repository = createRepository()
            val original = repository.getAllIngredients().first().first()

            val updated = original.copy(
                name = "Nombre Actualizado",
                allergens = setOf(AllergenType.GLUTEN, AllergenType.DAIRY),
            )

            repository.updateIngredient(updated)

            val result = repository.getAllIngredients().first()
            val found = result.first { it.id == original.id }
            assertEquals("Nombre Actualizado", found.name)
            assertEquals(setOf(AllergenType.GLUTEN, AllergenType.DAIRY), found.allergens)
        }

    @Test
    fun deleteIngredient_removesFromList() =
        runTest {
            val repository = createRepository()
            val initialList = repository.getAllIngredients().first()
            val toDelete = initialList.first()

            repository.deleteIngredient(toDelete.id)

            val updatedList = repository.getAllIngredients().first()
            assertEquals(initialList.size - 1, updatedList.size)
            assertFalse(updatedList.any { it.id == toDelete.id })
        }

    @Test
    fun searchIngredients_filtersByName() =
        runTest {
            val repository = createRepository()

            val results = repository.searchIngredients("Harina")

            assertTrue(results.isNotEmpty())
            assertTrue(results.all { it.name.contains("Harina", ignoreCase = true) })
        }

    @Test
    fun replaceAll_replacesEntireList() =
        runTest {
            val repository = createRepository()
            val newIngredients = listOf(
                Ingredient(
                    id = "new-1",
                    name = "Ingrediente nuevo",
                    allergens = setOf(AllergenType.GLUTEN),
                    createdAt = now,
                    updatedAt = now,
                ),
            )

            repository.replaceAll(newIngredients)

            val result = repository.getAllIngredients().first()
            assertEquals(1, result.size)
            assertEquals("new-1", result[0].id)
        }
}
