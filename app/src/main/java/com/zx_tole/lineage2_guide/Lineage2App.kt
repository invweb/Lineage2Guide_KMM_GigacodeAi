package com.zx_tole.lineage2_guide

import android.app.Application
import android.content.Context
import android.util.Log
import com.zx_tole.lineage2_guide.di.DefaultSettingsManager
import com.zx_tole.lineage2_guide.di.initKoin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class Lineage2App : Application() {

    companion object {
        @Volatile
        private var INSTANCE: Lineage2App? = null

        val instance: Lineage2App
            get() = INSTANCE ?: throw IllegalStateException("Lineage2App not initialized")

        fun Context.app(): Lineage2App = applicationContext as Lineage2App
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Use settings manager for configuration
        val settingsManager = DefaultSettingsManager()
        
        Log.d("Lineage2App", "Starting Koin with baseUrl: ${settingsManager.baseUrl}")

        initKoin(this, settingsManager)

        // Load initial data from server
        loadInitialData()
    }
    
    private fun loadInitialData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val koin = org.koin.core.context.GlobalContext.get()
                val itemsRepo = koin.get<com.zx_tole.lineage2_guide.domain.repository.ItemsRepository>()
                
                itemsRepo.refreshItems()
                Log.d("Lineage2App", "Items refresh completed")
            } catch (e: Exception) {
                Log.e("Lineage2App", "Failed to load initial data", e)
            }
        }
    }
}
