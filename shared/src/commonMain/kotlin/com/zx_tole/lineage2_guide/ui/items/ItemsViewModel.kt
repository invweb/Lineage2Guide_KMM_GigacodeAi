package com.zx_tole.lineage2_guide.ui.items

import com.zx_tole.lineage2_guide.common.Logger
import com.zx_tole.lineage2_guide.data.settings.SettingsManager
import com.zx_tole.lineage2_guide.domain.model.Item
import com.zx_tole.lineage2_guide.domain.model.ItemsFilter
import com.zx_tole.lineage2_guide.domain.repository.ItemsRepository
import com.zx_tole.lineage2_guide.ui.common.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ItemsUiState(
    val items: List<Item> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMore: Boolean = true,
    val error: String? = null,
    val searchQuery: String = "",
    val isRefreshing: Boolean = false
) {
    companion object {
        val Initial = ItemsUiState()
    }
}

class ItemsViewModel(
    private val itemsRepository: ItemsRepository,
    private val settingsManager: SettingsManager
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ItemsUiState.Initial)
    val uiState: StateFlow<ItemsUiState> = _uiState.asStateFlow()

    private var currentOffset = 0
    private var pageSize = 50

    init {
        loadItems()
    }

    private fun loadItems() {
        pageSize = settingsManager.getPageSize()
        currentOffset = 0
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                Logger.i("Loading items from API...")
                itemsRepository.refreshItems()
                loadItemsPage()
            } catch (e: Exception) {
                logError("Error in items load", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load items"
                )
            }
        }
    }

    private suspend fun loadItemsPage() {
        try {
            val items = itemsRepository.getItems(ItemsFilter()).firstOrNull() ?: emptyList()
            
            // Simulate pagination by taking only first pageSize items
            val pagedItems = if (items.size > currentOffset + pageSize) {
                items.take(currentOffset + pageSize)
            } else {
                items
            }
            
            _uiState.value = _uiState.value.copy(
                items = pagedItems,
                isLoading = false,
                isLoadingMore = false,
                hasMore = items.size > currentOffset + pageSize,
                error = null
            )
        } catch (e: Exception) {
            logError("Error loading items page", e)
        }
    }

    fun loadMore() {
        if (_uiState.value.isLoadingMore || !_uiState.value.hasMore) return
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingMore = true)
            currentOffset += pageSize
            loadItemsPage()
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query, items = emptyList())
        currentOffset = 0
        // Trigger reload
        viewModelScope.launch {
            try {
                itemsRepository.refreshItems()
                loadItemsPage()
            } catch (e: Exception) {
                logError("Error during search", e)
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            currentOffset = 0
            try {
                itemsRepository.refreshItems()
                loadItemsPage()
            } catch (e: Exception) {
                logError("Error during refresh", e)
                _uiState.value = _uiState.value.copy(
                    isRefreshing = false,
                    error = e.message ?: "Failed to refresh"
                )
            }
        }
    }
}
