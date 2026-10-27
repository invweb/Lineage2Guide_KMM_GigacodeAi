package com.zx_tole.lineage2_guide

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import java.io.File

@Composable
actual fun getAsyncImageLoader(): ImageLoader? {
    val context = LocalContext.current
    
    return ImageLoader.Builder(context)
        .memoryCachePolicy(coil3.request.CachePolicy.ENABLED)
        .diskCachePolicy(coil3.request.CachePolicy.ENABLED)
        .memoryCache {
            MemoryCache.Builder()
                .maxSizePercent(context, 0.25)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(File(context.cacheDir, "images"))
                .maxSizePercent(0.1)
                .build()
        }
        .build()
        .null
}
