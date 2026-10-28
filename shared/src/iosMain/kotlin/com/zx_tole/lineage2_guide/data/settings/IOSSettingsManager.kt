package com.zx_tole.lineage2_guide.data.settings

/**
 * iOS implementation of SettingsManager
 */
class IOSSettingsManager : SettingsManager {
    override var baseUrl: String = "http://localhost:8080"
    
    override fun setBaseUrl(url: String) {
        baseUrl = url
    }
    
    override fun getPageSize(): Int = 50
    
    override fun setPageSize(size: Int) {
        // Not used on iOS
    }
}
