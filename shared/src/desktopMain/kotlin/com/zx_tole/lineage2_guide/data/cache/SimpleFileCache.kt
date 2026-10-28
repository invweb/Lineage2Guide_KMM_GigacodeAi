package com.zx_tole.lineage2_guide.data.cache

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

/**
 * Desktop file-based cache implementation
 */
actual object SimpleFileCache {
    
    private val Json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
    
    private const val CACHE_DIR_NAME = "lineage2_cache"
    
    @Serializable
    data class CacheEntry<T>(
        val data: T,
        val timestamp: Long
    )
    
    actual suspend fun <T> getOrLoad(
        cacheKey: String,
        loader: suspend () -> T,
        maxAgeMs: Long
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
            // Silently fail
        }
    }
    
    actual suspend fun clearCache() {
        withContext(Dispatchers.IO) {
            try {
                val cacheDir = File(CACHE_DIR_NAME)
                if (cacheDir.exists()) {
                    cacheDir.deleteRecursively()
                }
            } catch (e: Exception) {
                // Ignore
            }
        }
    }
    
    private fun getCacheFile(filename: String): File {
        val cacheDir = File(CACHE_DIR_NAME)
        return File(cacheDir, filename)
    }
}
