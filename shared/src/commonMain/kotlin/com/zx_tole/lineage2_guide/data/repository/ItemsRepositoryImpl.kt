package com.zx_tole.lineage2_guide.data.repository

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
        val response = apiService.getItems()
        _items.value = response.items.map { itemMapper.toDomain(it) }
    }
    override suspend fun clearCache() { _items.value = emptyList() }
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
            limit = 10000
        )
        return response.items.size
    }
}
