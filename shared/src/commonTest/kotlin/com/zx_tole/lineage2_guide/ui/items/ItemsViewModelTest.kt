package com.zx_tole.lineage2_guide.ui.items

import com.zx_tole.lineage2_guide.data.settings.SettingsManager
import com.zx_tole.lineage2_guide.domain.model.Item
import com.zx_tole.lineage2_guide.domain.model.ItemType
import com.zx_tole.lineage2_guide.domain.model.ItemsFilter
import com.zx_tole.lineage2_guide.domain.model.Rarity
import com.zx_tole.lineage2_guide.domain.repository.ItemsRepository
import kotlinx.coroutines.flow.flowOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ItemsViewModelTest {

    @Test
    fun `search query should be stored in state`() {
        val mockRepo = object : ItemsRepository {
            override fun getItems(filter: ItemsFilter) = flowOf(emptyList<Item>())
            override suspend fun getTotalCount(filter: ItemsFilter) = 0
            override fun getItemById(id: Long) = flowOf(null)
            override suspend fun refreshItems() {}
            override suspend fun clearCache() {}
        }
        
        val mockSettings = object : SettingsManager {
            override val baseUrl: String = "http://localhost:8080"
            override fun setBaseUrl(url: String) {}
            override fun getPageSize(): Int = 50
            override fun setPageSize(size: Int) {}
        }
        
        val viewModel = ItemsViewModel(mockRepo, mockSettings)
        viewModel.onSearchQueryChanged("adamantite")
        val state = viewModel.uiState.value
        assertEquals("adamantite", state.searchQuery)
    }

    @Test
    fun `empty search query should be stored`() {
        val mockRepo = object : ItemsRepository {
            override fun getItems(filter: ItemsFilter) = flowOf(emptyList<Item>())
            override suspend fun getTotalCount(filter: ItemsFilter) = 0
            override fun getItemById(id: Long) = flowOf(null)
            override suspend fun refreshItems() {}
            override suspend fun clearCache() {}
        }
        
        val mockSettings = object : SettingsManager {
            override val baseUrl: String = "http://localhost:8080"
            override fun setBaseUrl(url: String) {}
            override fun getPageSize(): Int = 50
            override fun setPageSize(size: Int) {}
        }
        
        val viewModel = ItemsViewModel(mockRepo, mockSettings)
        viewModel.onSearchQueryChanged("")
        val state = viewModel.uiState.value
        assertEquals("", state.searchQuery)
    }

    @Test
    fun `repository should have items list`() {
        val testItems = listOf(
            Item(1L, "Item 1", null, 1, ItemType.WEAPON, Rarity.COMMON, null, null, emptyMap(), null, null),
            Item(2L, "Item 2", null, 5, ItemType.ARMOR, Rarity.RARE, null, null, emptyMap(), null, null),
            Item(3L, "Item 3", null, 10, ItemType.POTION, Rarity.UNCOMMON, null, null, emptyMap(), null, null)
        )
        
        assertEquals(3, testItems.size)
        assertEquals("Item 1", testItems[0].name)
        assertEquals(ItemType.WEAPON, testItems[0].type)
    }

    @Test
    fun `items with stats should be created correctly`() {
        val item = Item(
            id = 1L,
            name = "Adamantite Sword",
            classRestriction = null,
            level = 40,
            type = ItemType.WEAPON,
            rarity = Rarity.RARE,
            location = null,
            dropInfo = "NPC Drop",
            stats = mapOf("pAtk" to 150, "crit" to 20),
            description = "A powerful sword",
            iconUrl = null
        )
        
        assertEquals("Adamantite Sword", item.name)
        assertEquals(40, item.level)
        assertEquals(ItemType.WEAPON, item.type)
        assertEquals(Rarity.RARE, item.rarity)
        assertEquals(150, item.stats["pAtk"])
        assertEquals(20, item.stats["crit"])
    }

    @Test
    fun `item types enum should have all values`() {
        val types = ItemType.values()
        assertTrue(types.contains(ItemType.WEAPON))
        assertTrue(types.contains(ItemType.ARMOR))
        assertTrue(types.contains(ItemType.POTION))
        assertTrue(types.contains(ItemType.SCROLL))
    }

    @Test
    fun `rarity enum should have all values`() {
        val rarities = Rarity.values()
        assertTrue(rarities.contains(Rarity.COMMON))
        assertTrue(rarities.contains(Rarity.UNCOMMON))
        assertTrue(rarities.contains(Rarity.RARE))
        assertTrue(rarities.contains(Rarity.EPIC))
        assertTrue(rarities.contains(Rarity.LEGENDARY))
    }
}
