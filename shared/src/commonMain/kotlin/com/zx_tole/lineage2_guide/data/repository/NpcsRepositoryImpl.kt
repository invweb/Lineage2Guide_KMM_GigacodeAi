package com.zx_tole.lineage2_guide.data.repository

import com.zx_tole.lineage2_guide.data.cache.SimpleFileCache
import com.zx_tole.lineage2_guide.data.mapper.NpcMapper
import com.zx_tole.lineage2_guide.data.network.Lineage2ApiService
import com.zx_tole.lineage2_guide.domain.model.Npc
import com.zx_tole.lineage2_guide.domain.repository.NpcsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class NpcsRepositoryImpl(
    private val apiService: Lineage2ApiService,
    private val npcMapper: NpcMapper
) : NpcsRepository {
    private val _npcs = MutableStateFlow<List<Npc>>(emptyList())
    override fun getNpcs(type: String?, location: String?, searchQuery: String?): Flow<List<Npc>> = _npcs.asStateFlow()
    override fun getNpcById(id: Long): Flow<Npc?> = flow { emit(_npcs.value.find { it.id == id }) }
    
    override suspend fun refreshNpcs() {
        try {
            _npcs.value = SimpleFileCache.getOrLoad(
                cacheKey = "npcs",
                loader = { loadNpcsFromApi() },
                maxAgeMs = 24 * 60 * 60 * 1000
            )
        } catch (e: Exception) {
            _npcs.value = emptyList()
            throw e
        }
    }
    
    private suspend fun loadNpcsFromApi(): List<Npc> {
        val firstPage = apiService.getNpcs(offset = 0, limit = 100)
        var allNpcs = mutableMapOf<Long, Npc>()
        
        for (npc in firstPage.npcs) {
            val domainNpc = npcMapper.toDomain(npc)
            allNpcs[domainNpc.id] = domainNpc
        }
        
        val totalCount = firstPage.total ?: firstPage.npcs.size
        var offset = 100
        
        while (offset < totalCount && offset < 1000) {
            try {
                val page = apiService.getNpcs(offset = offset, limit = 100)
                for (npc in page.npcs) {
                    val domainNpc = npcMapper.toDomain(npc)
                    allNpcs[domainNpc.id] = domainNpc
                }
                offset += 100
            } catch (e: Exception) {
                break
            }
        }
        
        return allNpcs.values.toList()
    }
    
    override suspend fun clearCache() { 
        _npcs.value = emptyList()
        SimpleFileCache.clearCache()
    }
}
