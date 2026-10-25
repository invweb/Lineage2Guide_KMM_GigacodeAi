package com.zx_tole.lineage2_guide.ui.items

import com.zx_tole.lineage2_guide.common.Logger
import com.zx_tole.lineage2_guide.domain.model.Item
import com.zx_tole.lineage2_guide.domain.model.ItemsFilter
import com.zx_tole.lineage2_guide.domain.repository.ItemsRepository
import com.zx_tole.lineage2_guide.ui.common.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ItemsUiState(
    val items: List<Item> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val isRefreshing: Boolean = false
) {
    companion object {
        val Initial = ItemsUiState()
    }
}

class ItemsViewModel(
    private val itemsRepository: ItemsRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ItemsUiState.Initial)
    val uiState: StateFlow<ItemsUiState> = _uiState.asStateFlow()

    init {
        loadItems()
    }

    private fun loadItems() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                Logger.i("Loading items from API...")
                // refreshItems loads data from API into repository's internal state
                itemsRepository.refreshItems()
                Logger.i("Items loaded, fetching...")
                // getItems returns a Flow that emits the current items
                // We collect once to get initial value, then stop
                val items = itemsRepository.getItems(ItemsFilter()).firstOrNull() ?: emptyList()
                Logger.i("Items count: ${items.size}")
                _uiState.value = _uiState.value.copy(
                    items = items,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                logError("Error in items load", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load items"
                )
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        // Trigger search by reloading items (repository will filter if needed)
        if (query.isNotBlank()) {
            viewModelScope.launch {
                try {
                    // In a real app, this would pass search to API
                    // For now, just update the UI state
                    Logger.i("Search query changed: $query")
                } catch (e: Exception) {
                    logError("Error during search", e)
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            try {
                itemsRepository.refreshItems()
                val items = itemsRepository.getItems(ItemsFilter()).firstOrNull() ?: emptyList()
                _uiState.value = _uiState.value.copy(
                    items = items,
                    isRefreshing = false,
                    error = null
                )
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
