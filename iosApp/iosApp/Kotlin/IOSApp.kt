package com.zx_tole.lineage2_guide.ios

import androidx.compose.runtime.Composable
import com.zx_tole.lineage2_guide.di.KoinHelper
import com.zx_tole.lineage2_guide.ui.IOSAppContent
import com.zx_tole.lineage2_guide.ui.theme.Lineage2Theme
import org.koin.core.component.KoinComponent

private const val DEFAULT_BASE_URL = "http://localhost:8080"

@Composable
fun IOSApp() {
    // Initialize Koin if not already initialized
    if (!KoinHelper.initialized) {
        KoinHelper.initKoin(DEFAULT_BASE_URL)
    }
    
    IOSAppContent()
}

class IOSAppKoinInitializer : KoinComponent {
    fun initialize(baseUrl: String = DEFAULT_BASE_URL) {
        KoinHelper.initKoin(baseUrl)
    }
}
