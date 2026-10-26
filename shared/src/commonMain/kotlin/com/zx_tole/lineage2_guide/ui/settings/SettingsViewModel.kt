package com.zx_tole.lineage2_guide.ui.settings

import com.zx_tole.lineage2_guide.common.Logger
import com.zx_tole.lineage2_guide.data.settings.SettingsManager
import com.zx_tole.lineage2_guide.ui.common.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SettingsUiState(
    val baseUrl: String = "",
    val pageSize: Int = 50,
    val isSaving: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)

class SettingsViewModel(
    private val settingsManager: SettingsManager
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        _uiState.value = _uiState.value.copy(
            baseUrl = settingsManager.baseUrl,
            pageSize = settingsManager.getPageSize()
        )
    }

    fun onBaseUrlChanged(url: String) {
        _uiState.value = _uiState.value.copy(baseUrl = url, errorMessage = null)
    }

    fun onPageSizeChanged(size: String) {
        _uiState.value = _uiState.value.copy(pageSize = size.toIntOrNull() ?: 50)
    }

    fun saveSettings() {
        val current = _uiState.value
        if (current.baseUrl.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "URL cannot be empty")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, successMessage = null, errorMessage = null)
            try {
                settingsManager.setBaseUrl(current.baseUrl)
                settingsManager.setPageSize(current.pageSize)
                
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    successMessage = "Settings saved successfully!"
                )
                
                Logger.i("Settings saved: baseUrl=${current.baseUrl}, pageSize=${current.pageSize}")
                
                // Clear success message after 3 seconds
                kotlinx.coroutines.delay(3000)
                _uiState.value = _uiState.value.copy(successMessage = null)
                
            } catch (e: Exception) {
                logError("Error saving settings", e)
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    errorMessage = "Failed to save settings: ${e.message}"
                )
            }
        }
    }

    fun clearSuccessMessage() {
        _uiState.value = _uiState.value.copy(successMessage = null)
    }
}
