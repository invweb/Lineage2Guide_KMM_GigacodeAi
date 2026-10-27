package com.zx_tole.lineage2_guide.data.repository

import com.zx_tole.lineage2_guide.data.cache.SimpleFileCache
import com.zx_tole.lineage2_guide.data.mapper.ItemMapper
import com.zx_tole.lineage2_guide.data.network.Lineage2ApiService
import com.zx_tole.lineage2_guide.domain.model.Item
import com.zx_tole.lineage2_guide.domain.model.ItemsFilter
import com.zx_tole.lineage2_guide.domain.repository.ItemsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class ItemsRepositoryImpl(
    private val apiService: Lineage2ApiService,
    private val itemMapper: ItemMapper
) : ItemsRepository {

    private val _items = MutableStateFlow<List<Item>>(emptyList())
    override fun getItems(filter: ItemsFilter): Flow<List<Item>> = _items.asStateFlow()
    override fun getItemById(id: Long): Flow<Item?> = flow { emit(_items.value.find { it.id == id }) }
    
    override suspend fun refreshItems() {
        try {
            _items.value = SimpleFileCache.getOrLoad(
                cacheKey = "items",
                loader = { loadItemsFromApi() },
                maxAgeMs = 24 * 60 * 60 * 1000
            )
        } catch (e: Exception) {
            _items.value = emptyList()
            throw e
        }
    }
    
    private suspend fun loadItemsFromApi(): List<Item> {
        val firstPage = apiService.getItems(offset = 0, limit = 100)
        var allItems = mutableMapOf<Long, Item>()
        
        for (item in firstPage.items) {
            val domainItem = itemMapper.toDomain(item)
            allItems[domainItem.id] = domainItem
        }
        
        val totalCount = firstPage.total ?: firstPage.items.size
        var offset = 100
        
        while (offset < totalCount && offset < 1000) {
            try {
                val page = apiService.getItems(offset = offset, limit = 100)
                for (item in page.items) {
                    val domainItem = itemMapper.toDomain(item)
                    allItems[domainItem.id] = domainItem
                }
                offset += 100
            } catch (e: Exception) {
                break
            }
        }
        
        return allItems.values.toList()
    }
    
    override suspend fun clearCache() { 
        _items.value = emptyList()
        SimpleFileCache.clearCache()
    }
    override suspend fun getTotalCount(filter: ItemsFilter): Int {
        val response = apiService.getItems(
            classRestriction = filter.classRestriction,
            minLevel = filter.minLevel,
            maxLevel = filter.maxLevel,
            type = filter.type,
            rarity = filter.rarity,
            location = filter.location,
            search = filter.searchQuery,
            offset = 0,
            limit = 1
        )
        return response.total ?: response.items.size
    }
}
