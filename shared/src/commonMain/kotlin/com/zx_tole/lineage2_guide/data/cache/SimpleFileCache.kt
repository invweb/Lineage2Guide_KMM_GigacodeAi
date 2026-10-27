package com.zx_tole.lineage2_guide.data.cache

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Paths

/**
 * Simple file-based cache for storing loaded data
 * Works on all platforms (Android, Desktop, iOS)
 */
object SimpleFileCache {
    
    private val Json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
    
    private const val CACHE_DIR_NAME = "lineage2_cache"
    private const val ITEMS_CACHE = "items.json"
    private const val NPCS_CACHE = "npcs.json"
    private const val SKILLS_CACHE = "skills.json"
    private const val QUESTS_CACHE = "quests.json"
    private const val CLASSES_CACHE = "classes.json"
    
    @Serializable
    data class CacheEntry<T>(
        val data: T,
        val timestamp: Long
    )
    
    suspend fun <T> getOrLoad(
        cacheKey: String,
        loader: suspend () -> T,
        maxAgeMs: Long = 24 * 60 * 60 * 1000 // 24 hours
    ): T {
        return withContext(Dispatchers.IO) {
            val cached = readCache(cacheKey)
            if (cached != null && (System.currentTimeMillis() - cached.timestamp) < maxAgeMs) {
                @Suppress("UNCHECKED_CAST")
                cached.data as T
            } else {
                val newData = loader()
                writeCache(cacheKey, CacheEntry(newData, System.currentTimeMillis()))
                newData
            }
        }
    }
    
    private fun readCache(cacheKey: String): CacheEntry<*>? {
        return try {
            val cacheFile = getCacheFile(cacheKey)
            if (cacheFile.exists() && cacheFile.isFile) {
                val json = cacheFile.readText()
                Json.decodeFromString<CacheEntry<*>>(json)
            } else null
        } catch (e: Exception) {
            null
        }
    }
    
    @Suppress("UNCHECKED_CAST")
    private fun <T> writeCache(cacheKey: String, entry: CacheEntry<T>) {
        try {
            val cacheFile = getCacheFile(cacheKey)
            cacheFile.parentFile?.mkdirs()
            cacheFile.writeText(Json.encodeToString(entry))
        } catch (e: Exception) {
            // Silently fail - network will be used if cache fails
        }
    }
    
    suspend fun clearCache() {
        withContext(Dispatchers.IO) {
            listOf(ITEMS_CACHE, NPCS_CACHE, SKILLS_CACHE, QUESTS_CACHE, CLASSES_CACHE).forEach { key ->
                try {
                    val cacheFile = getCacheFile(key)
                    if (cacheFile.exists()) {
                        cacheFile.delete()
                    }
                } catch (e: Exception) {
                    // Ignore
                }
            }
        }
    }
    
    private fun getCacheFile(filename: String): File {
        val cacheDir = File(CACHE_DIR_NAME)
        return File(cacheDir, filename)
    }
}
