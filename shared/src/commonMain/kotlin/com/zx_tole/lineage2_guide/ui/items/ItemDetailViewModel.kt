package com.zx_tole.lineage2_guide.ui.items

import com.zx_tole.lineage2_guide.domain.repository.ItemsRepository
import com.zx_tole.lineage2_guide.ui.common.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ItemDetailUiState(
    val item: com.zx_tole.lineage2_guide.domain.model.Item? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class ItemDetailViewModel(
    private val itemsRepository: ItemsRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ItemDetailUiState(isLoading = true))
    val uiState: StateFlow<ItemDetailUiState> = _uiState.asStateFlow()

    fun loadItem(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val item = itemsRepository.getItemById(id).first()
                if (item != null) {
                    _uiState.update { it.copy(item = item, isLoading = false, error = null) }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Item not found") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
