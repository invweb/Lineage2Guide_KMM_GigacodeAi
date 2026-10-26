package com.zx_tole.lineage2_guide.ui.settings

import com.zx_tole.lineage2_guide.data.settings.SettingsManager
import kotlin.test.Test
import kotlin.test.assertEquals

class SettingsViewModelTest {

    @Test
    fun `settings should have default values`() {
        val mockSettings = object : SettingsManager {
            override val baseUrl: String = "http://localhost:8080"
            override fun setBaseUrl(url: String) {}
            override fun getPageSize(): Int = 50
            override fun setPageSize(size: Int) {}
        }

        val viewModel = SettingsViewModel(mockSettings)
        val state = viewModel.uiState.value

        assertEquals("http://localhost:8080", state.baseUrl)
        assertEquals(50, state.pageSize)
    }

    @Test
    fun `should update base URL on change`() {
        val mockSettings = object : SettingsManager {
            override val baseUrl: String = "http://localhost:8080"
            override fun setBaseUrl(url: String) {}
            override fun getPageSize(): Int = 50
            override fun setPageSize(size: Int) {}
        }

        val viewModel = SettingsViewModel(mockSettings)
        viewModel.onBaseUrlChanged("http://192.168.1.100:8080")
        
        val state = viewModel.uiState.value
        assertEquals("http://192.168.1.100:8080", state.baseUrl)
    }

    @Test
    fun `should update page size on change`() {
        val mockSettings = object : SettingsManager {
            override val baseUrl: String = "http://localhost:8080"
            override fun setBaseUrl(url: String) {}
            override fun getPageSize(): Int = 50
            override fun setPageSize(size: Int) {}
        }

        val viewModel = SettingsViewModel(mockSettings)
        viewModel.onPageSizeChanged("100")
        
        val state = viewModel.uiState.value
        assertEquals(100, state.pageSize)
    }

    @Test
    fun `invalid page size should default to 50`() {
        val mockSettings = object : SettingsManager {
            override val baseUrl: String = "http://localhost:8080"
            override fun setBaseUrl(url: String) {}
            override fun getPageSize(): Int = 50
            override fun setPageSize(size: Int) {}
        }

        val viewModel = SettingsViewModel(mockSettings)
        viewModel.onPageSizeChanged("invalid")
        
        val state = viewModel.uiState.value
        assertEquals(50, state.pageSize)
    }

    @Test
    fun `settings with custom values should load correctly`() {
        val mockSettings = object : SettingsManager {
            override val baseUrl: String = "http://custom-server:3000"
            override fun setBaseUrl(url: String) {}
            override fun getPageSize(): Int = 100
            override fun setPageSize(size: Int) {}
        }

        val viewModel = SettingsViewModel(mockSettings)
        val state = viewModel.uiState.value

        assertEquals("http://custom-server:3000", state.baseUrl)
        assertEquals(100, state.pageSize)
    }
}
