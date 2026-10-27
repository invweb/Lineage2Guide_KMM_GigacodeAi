package com.zx_tole.lineage2_guide.data.repository

import com.zx_tole.lineage2_guide.data.cache.SimpleFileCache
import com.zx_tole.lineage2_guide.data.mapper.ClassMapper
import com.zx_tole.lineage2_guide.data.network.Lineage2ApiService
import com.zx_tole.lineage2_guide.domain.model.GameClass
import com.zx_tole.lineage2_guide.domain.repository.ClassesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class ClassesRepositoryImpl(
    private val apiService: Lineage2ApiService,
    private val classMapper: ClassMapper
) : ClassesRepository {
    private val _classes = MutableStateFlow<List<GameClass>>(emptyList())
    override fun getClasses(race: String?, searchQuery: String?): Flow<List<GameClass>> = _classes.asStateFlow()
    override fun getClassById(id: Long): Flow<GameClass?> = flow { emit(_classes.value.find { it.id == id }) }
    
    override suspend fun refreshClasses() {
        try {
            _classes.value = SimpleFileCache.getOrLoad(
                cacheKey = "classes",
                loader = { loadClassesFromApi() },
                maxAgeMs = 24 * 60 * 60 * 1000
            )
        } catch (e: Exception) {
            _classes.value = emptyList()
            throw e
        }
    }
    
    private suspend fun loadClassesFromApi(): List<GameClass> {
        val firstPage = apiService.getClasses(offset = 0, limit = 50)
        var allClasses = mutableMapOf<Long, GameClass>()
        
        for (classData in firstPage.classes) {
            val domainClass = classMapper.toDomain(classData)
            allClasses[domainClass.id] = domainClass
        }
        
        val totalCount = firstPage.total ?: firstPage.classes.size
        var offset = 50
        
        while (offset < totalCount) {
            try {
                val page = apiService.getClasses(offset = offset, limit = 50)
                for (classData in page.classes) {
                    val domainClass = classMapper.toDomain(classData)
                    allClasses[domainClass.id] = domainClass
                }
                offset += 50
            } catch (e: Exception) {
                break
            }
        }
        
        return allClasses.values.toList()
    }
    
    override suspend fun clearCache() { 
        _classes.value = emptyList()
        SimpleFileCache.clearCache()
    }
}
