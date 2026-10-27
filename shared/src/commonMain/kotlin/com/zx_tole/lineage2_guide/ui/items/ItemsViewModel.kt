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
            val allItems = itemsRepository.getItems(ItemsFilter()).firstOrNull() ?: emptyList()
            val end_index = minOf(currentOffset + pageSize, allItems.size)
            val newItems = allItems.subList(0, end_index)
            
            _uiState.value = _uiState.value.copy(
                items = newItems,
                isLoading = false,
                isLoadingMore = false,
                hasMore = end_index < allItems.size,
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
            val oldItems = _uiState.value.items
            val newOffset = currentOffset + pageSize
            
            try {
                val allItems = itemsRepository.getItems(ItemsFilter()).firstOrNull() ?: emptyList()
                val end_index = minOf(newOffset + pageSize, allItems.size)
                val newItemsSlice = allItems.subList(currentOffset, end_index)
                
                _uiState.value = _uiState.value.copy(
                    items = oldItems + newItemsSlice,
                    isLoadingMore = false,
                    isLoading = false,
                    hasMore = end_index < allItems.size,
                    error = null
                )
                currentOffset = newOffset
            } catch (e: Exception) {
                logError("Error loading more items", e)
                _uiState.value = _uiState.value.copy(isLoadingMore = false)
            }
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
