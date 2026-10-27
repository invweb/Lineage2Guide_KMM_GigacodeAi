package com.zx_tole.lineage2_guide

import androidx.compose.ui.platform.LocalContext
import coil3.ImageLoader
import coil3.disk.DiskCache
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.encodePath
import io.ktor.serialization.kotlinx.json.Json
import java.io.File

/**
 * Creates a configured Coil ImageLoader for Desktop
 * Uses Ktor HTTP client for loading images from server
 */
fun createDesktopImageLoader(): ImageLoader {
    val ktorClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        
        // Allow HTTP requests to localhost
        engine {
            connectionTimeout = 10000
            requestTimeout = 30000
        }
    }
    
    return ImageLoader.Builder(LocalContext.current)
        .components {
            add(io.coil3.kt.image.network.UrlDecoder { url ->
                // Decode URL to handle special characters
                io.coil3.request.allowHardware(false)
                url
            })
        }
        .networkLoader(
            factory = {
                io.coil3.kt.image.network.KtorImageLoader(ktorClient)
            }
        )
        .diskCache(
            DiskCache.Builder()
                .directory(File(System.getProperty("java.io.tmpdir"), "lineage2_cache/images"))
                .maxSizePercent(0.1)
                .build()
        )
        .build()
}
