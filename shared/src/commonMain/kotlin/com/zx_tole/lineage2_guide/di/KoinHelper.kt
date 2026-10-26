package com.zx_tole.lineage2_guide.di

import com.zx_tole.lineage2_guide.data.settings.SettingsManager
import com.zx_tole.lineage2_guide.di.sharedModule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

object KoinHelper {

    private var _initialized = false
    val initialized: Boolean
        get() = _initialized

    fun initKoin(settingsManager: SettingsManager = DefaultSettingsManager()) {
        if (_initialized) return
        stopKoin()
        startKoin {
            modules(sharedModule(settingsManager))
        }
        _initialized = true
    }

    fun shutdownKoin() {
        stopKoin()
        _initialized = false
    }
}

/**
 * Default settings manager using hardcoded values
 * Platform-specific implementations should override this
 */
class DefaultSettingsManager : SettingsManager {
    override val baseUrl: String = "http://localhost:8080"
    override fun setBaseUrl(url: String) {
        // No-op in default implementation
    }
    override fun getPageSize(): Int = 50
    override fun setPageSize(size: Int) {
        // No-op in default implementation
    }
}
