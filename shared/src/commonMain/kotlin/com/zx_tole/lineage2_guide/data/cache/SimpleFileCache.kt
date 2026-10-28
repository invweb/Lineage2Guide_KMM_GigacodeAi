package com.zx_tole.lineage2_guide.data.cache

/**
 * File-based cache - platform specific implementations
 */
expect object SimpleFileCache {
    suspend fun <T> getOrLoad(
        cacheKey: String,
        loader: suspend () -> T,
        maxAgeMs: Long
    ): T
    
    suspend fun clearCache()
}
