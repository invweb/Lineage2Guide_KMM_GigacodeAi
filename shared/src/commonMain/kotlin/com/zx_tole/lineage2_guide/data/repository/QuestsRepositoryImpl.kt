package com.zx_tole.lineage2_guide.data.repository

import com.zx_tole.lineage2_guide.data.cache.SimpleFileCache
import com.zx_tole.lineage2_guide.data.mapper.QuestMapper
import com.zx_tole.lineage2_guide.data.network.Lineage2ApiService
import com.zx_tole.lineage2_guide.domain.model.Quest
import com.zx_tole.lineage2_guide.domain.repository.QuestsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class QuestsRepositoryImpl(
    private val apiService: Lineage2ApiService,
    private val questMapper: QuestMapper
) : QuestsRepository {
    private val _quests = MutableStateFlow<List<Quest>>(emptyList())
    override fun getQuests(type: String?, startLevel: Int?, npcId: Long?): Flow<List<Quest>> = _quests.asStateFlow()
    override fun getQuestById(id: Long): Flow<Quest?> = flow { emit(_quests.value.find { it.id == id }) }
    
    override suspend fun refreshQuests() {
        try {
            _quests.value = SimpleFileCache.getOrLoad(
                cacheKey = "quests",
                loader = { loadQuestsFromApi() },
                maxAgeMs = 24 * 60 * 60 * 1000
            )
        } catch (e: Exception) {
            _quests.value = emptyList()
            throw e
        }
    }
    
    private suspend fun loadQuestsFromApi(): List<Quest> {
        val firstPage = apiService.getQuests(offset = 0, limit = 50)
        var allQuests = mutableMapOf<Long, Quest>()
        
        for (quest in firstPage.quests) {
            val domainQuest = questMapper.toDomain(quest)
            allQuests[domainQuest.id] = domainQuest
        }
        
        val totalCount = firstPage.total ?: firstPage.quests.size
        var offset = 50
        
        while (offset < totalCount) {
            try {
                val page = apiService.getQuests(offset = offset, limit = 50)
                for (quest in page.quests) {
                    val domainQuest = questMapper.toDomain(quest)
                    allQuests[domainQuest.id] = domainQuest
                }
                offset += 50
            } catch (e: Exception) {
                break
            }
        }
        
        return allQuests.values.toList()
    }
    
    override suspend fun clearCache() { 
        _quests.value = emptyList()
        SimpleFileCache.clearCache()
    }
}
