package com.zx_tole.lineage2_guide.data.repository

import com.zx_tole.lineage2_guide.data.cache.SimpleFileCache
import com.zx_tole.lineage2_guide.data.mapper.SkillMapper
import com.zx_tole.lineage2_guide.data.network.Lineage2ApiService
import com.zx_tole.lineage2_guide.domain.model.Skill
import com.zx_tole.lineage2_guide.domain.repository.SkillsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class SkillsRepositoryImpl(
    private val apiService: Lineage2ApiService,
    private val skillMapper: SkillMapper
) : SkillsRepository {
    private val _skills = MutableStateFlow<List<Skill>>(emptyList())
    override fun getSkills(classRestriction: String?, type: String?, minLevel: Int?, maxLevel: Int?): Flow<List<Skill>> = _skills.asStateFlow()
    override fun getSkillById(id: Long): Flow<Skill?> = flow { emit(_skills.value.find { it.id == id }) }
    
    override suspend fun refreshSkills() {
        try {
            _skills.value = SimpleFileCache.getOrLoad(
                cacheKey = "skills",
                loader = { loadSkillsFromApi() },
                maxAgeMs = 24 * 60 * 60 * 1000
            )
        } catch (e: Exception) {
            _skills.value = emptyList()
            throw e
        }
    }
    
    private suspend fun loadSkillsFromApi(): List<Skill> {
        val firstPage = apiService.getSkills(offset = 0, limit = 100)
        var allSkills = mutableMapOf<Long, Skill>()
        
        for (skill in firstPage.skills) {
            val domainSkill = skillMapper.toDomain(skill)
            allSkills[domainSkill.id] = domainSkill
        }
        
        val totalCount = firstPage.total ?: firstPage.skills.size
        var offset = 100
        
        while (offset < totalCount && offset < 1000) {
            try {
                val page = apiService.getSkills(offset = offset, limit = 100)
                for (skill in page.skills) {
                    val domainSkill = skillMapper.toDomain(skill)
                    allSkills[domainSkill.id] = domainSkill
                }
                offset += 100
            } catch (e: Exception) {
                break
            }
        }
        
        return allSkills.values.toList()
    }
    
    override suspend fun clearCache() { 
        _skills.value = emptyList()
        SimpleFileCache.clearCache()
    }
}
