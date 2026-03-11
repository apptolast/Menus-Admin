package org.apptolast.menuadmin.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import org.apptolast.menuadmin.data.local.MockDataProvider
import org.apptolast.menuadmin.domain.model.Menu
import org.apptolast.menuadmin.domain.repository.MenuRepository

class MockMenuRepository(
    private val json: Json,
) : MenuRepository {
    private val _menus = MutableStateFlow(MockDataProvider.menus)

    override fun getAllMenus(): Flow<List<Menu>> = _menus.asStateFlow()

    override suspend fun getMenuById(id: String): Menu? {
        return _menus.value.find { it.id == id }
    }

    override suspend fun addMenu(menu: Menu): Menu {
        _menus.update { current -> current + menu }
        return menu
    }

    override suspend fun updateMenu(menu: Menu): Menu {
        _menus.update { current ->
            current.map { if (it.id == menu.id) menu else it }
        }
        return menu
    }

    override suspend fun deleteMenu(id: String) {
        _menus.update { current -> current.filter { it.id != id } }
    }

    override suspend fun exportMenuToJson(id: String): String {
        val menu = _menus.value.find { it.id == id }
            ?: throw NoSuchElementException("Menu with id $id not found")
        return json.encodeToString(Menu.serializer(), menu)
    }

    override suspend fun importMenuFromJson(json: String): Menu {
        val menu = this.json.decodeFromString(Menu.serializer(), json)
        _menus.update { current -> current + menu }
        return menu
    }
}
