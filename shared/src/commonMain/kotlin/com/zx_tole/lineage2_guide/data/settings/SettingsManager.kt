package com.zx_tole.lineage2_guide.data.settings

import kotlinx.serialization.Serializable

/**
 * Settings manager for app configuration
 */
interface SettingsManager {
    val baseUrl: String
    fun setBaseUrl(url: String)
    fun getPageSize(): Int
    fun setPageSize(size: Int)
}

@Serializable
data class AppSettings(
    val baseUrl: String = "http://localhost:8080",
    val pageSize: Int = 50
) {
    companion object {
        val Default = AppSettings()
    }
}
