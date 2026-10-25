package com.zx_tole.lineage2_guide.di

import com.zx_tole.lineage2_guide.di.sharedModule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

object KoinHelper {

    private var _initialized = false
    val initialized: Boolean
        get() = _initialized

    fun initKoin(baseUrl: String) {
        if (_initialized) return
        stopKoin()
        startKoin {
            modules(sharedModule(baseUrl))
        }
        _initialized = true
    }

    fun shutdownKoin() {
        stopKoin()
        _initialized = false
    }
}
