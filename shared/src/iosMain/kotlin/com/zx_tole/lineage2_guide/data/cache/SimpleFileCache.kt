package com.zx_tole.lineage2_guide.data.cache

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * iOS file-based cache (no-op implementation)
 * iOS uses memory cache only
 */
actual object SimpleFileCache {
    
    actual suspend fun <T> getOrLoad(
        cacheKey: String,
        loader: suspend () -> T,
        maxAgeMs: Long
    ): T {
        // No file cache on iOS, just load directly
        return loader()
    }
    
    actual suspend fun clearCache() {
        withContext(Dispatchers.Default) {
            // No-op
        }
    }
}
