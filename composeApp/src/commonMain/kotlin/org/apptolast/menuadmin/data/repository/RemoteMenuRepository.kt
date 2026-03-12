package org.apptolast.menuadmin.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import org.apptolast.menuadmin.data.remote.dish.DishService
import org.apptolast.menuadmin.data.remote.mapper.toDomain
import org.apptolast.menuadmin.data.remote.menu.MenuRequestDto
import org.apptolast.menuadmin.data.remote.menu.MenuService
import org.apptolast.menuadmin.data.remote.menu.SectionRequestDto
import org.apptolast.menuadmin.domain.model.Menu
import org.apptolast.menuadmin.domain.model.Section
import org.apptolast.menuadmin.domain.repository.MenuRepository

class RemoteMenuRepository(
    private val menuService: MenuService,
    private val dishService: DishService,
    private val json: Json,
) : MenuRepository {
    private val _menus = MutableStateFlow<List<Menu>>(emptyList())
    private var hasLoaded = false

    override fun getAllMenus(): Flow<List<Menu>> =
        flow {
            if (!hasLoaded) {
                try {
                    refreshMenus()
                } catch (_: Exception) {
                    // Emit empty list on failure; the UI will handle it
                }
            }
            emitAll(_menus)
        }

    override fun getMenusByRestaurant(restaurantId: String): Flow<List<Menu>> =
        flow {
            if (!hasLoaded) {
                try {
                    refreshMenus()
                } catch (_: Exception) {
                }
            }
            emitAll(
                _menus.map { menus ->
                    menus.filter { it.restaurantId == restaurantId }
                },
            )
        }

    suspend fun refreshMenus() {
        val menuDtos = menuService.getMenus()
        val dishDtos = dishService.getDishes()

        // Group dishes by sectionId
        val domainDishes = dishDtos.map { it.toDomain() }
        val dishesBySection = domainDishes.groupBy { it.sectionId }

        _menus.value = menuDtos.map { menuDto ->
            val sections = menuDto.sections.map { sectionDto ->
                val sectionDishes = dishesBySection[sectionDto.id] ?: emptyList()
                Section(
                    id = sectionDto.id,
                    name = sectionDto.name,
                    displayOrder = sectionDto.displayOrder,
                    dishes = sectionDishes,
                )
            }
            val allDishes = sections.flatMap { it.dishes }
            Menu(
                id = menuDto.id,
                name = menuDto.name,
                description = menuDto.description,
                displayOrder = menuDto.displayOrder,
                sections = sections,
                archived = menuDto.archived,
                dishes = allDishes,
            )
        }
        hasLoaded = true
    }

    override suspend fun getMenuById(id: String): Menu? {
        if (!hasLoaded) refreshMenus()
        return _menus.value.find { it.id == id }
    }

    override suspend fun addMenu(menu: Menu): Menu {
        val response = menuService.createMenu(
            MenuRequestDto(
                name = menu.name,
                description = menu.description.ifEmpty { null },
                displayOrder = menu.displayOrder,
            ),
        )
        val created = response.toDomain()
        refreshMenus()
        return created
    }

    override suspend fun updateMenu(menu: Menu): Menu {
        val response = menuService.updateMenu(
            menu.id,
            MenuRequestDto(
                name = menu.name,
                description = menu.description.ifEmpty { null },
                displayOrder = menu.displayOrder,
            ),
        )
        val updated = response.toDomain()
        refreshMenus()
        return updated
    }

    override suspend fun deleteMenu(id: String) {
        menuService.archiveMenu(id)
        refreshMenus()
    }

    // Sections
    suspend fun addSection(
        menuId: String,
        name: String,
        displayOrder: Int,
    ): Section {
        val response = menuService.createSection(
            menuId,
            SectionRequestDto(name = name, displayOrder = displayOrder),
        )
        refreshMenus()
        return response.toDomain()
    }

    suspend fun updateSection(
        menuId: String,
        sectionId: String,
        name: String,
        displayOrder: Int,
    ): Section {
        val response = menuService.updateSection(
            menuId,
            sectionId,
            SectionRequestDto(name = name, displayOrder = displayOrder),
        )
        refreshMenus()
        return response.toDomain()
    }

    suspend fun deleteSection(
        menuId: String,
        sectionId: String,
    ) {
        menuService.deleteSection(menuId, sectionId)
        refreshMenus()
    }

    override suspend fun exportMenuToJson(id: String): String {
        val menu = getMenuById(id) ?: throw NoSuchElementException("Menu $id not found")
        return json.encodeToString(Menu.serializer(), menu)
    }

    override suspend fun importMenuFromJson(json: String): Menu {
        return this.json.decodeFromString(Menu.serializer(), json)
    }
}
