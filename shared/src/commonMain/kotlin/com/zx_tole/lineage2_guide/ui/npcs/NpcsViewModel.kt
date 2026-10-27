package com.zx_tole.lineage2_guide.ui.npcs

import com.zx_tole.lineage2_guide.domain.model.Npc
import com.zx_tole.lineage2_guide.domain.repository.NpcsRepository
import com.zx_tole.lineage2_guide.ui.common.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class NpcsUiState(
    val npcs: List<Npc> = emptyList(),
    val allNpcs: List<Npc> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)

class NpcsViewModel(
    private val npcsRepository: NpcsRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(NpcsUiState(isLoading = true))
    val uiState: StateFlow<NpcsUiState> = _uiState.asStateFlow()

    init {
        loadNpcs()
    }

    private fun loadNpcs() {
        viewModelScope.launch {
            try {
                npcsRepository.refreshNpcs()
                val npcs = npcsRepository.getNpcs(null, null, null).firstOrNull() ?: emptyList()
                _uiState.update {
                    it.copy(
                        allNpcs = npcs,
                        npcs = npcs,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                logError("Error loading npcs", e)
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            try {
                npcsRepository.getNpcs(null, null, null)
                    .catch { e ->
                        logError("Error refreshing npcs", e)
                    }
                    .collect { npcs ->
                        val query = _uiState.value.searchQuery
                        val filtered = if (query.isNotBlank()) {
                            npcs.filter { n ->
                                n.name.contains(query, ignoreCase = true) ||
                                    n.type.name.contains(query, ignoreCase = true) ||
                                    n.location.contains(query, ignoreCase = true) ||
                                    n.description?.contains(query, ignoreCase = true) == true
                            }
                        } else {
                            npcs
                        }
                        _uiState.update {
                            it.copy(
                                allNpcs = npcs,
                                npcs = filtered,
                                isRefreshing = false,
                                error = null
                            )
                        }
                    }
            } catch (e: Exception) {
                logError("Error in refresh", e)
                _uiState.update { it.copy(isRefreshing = false, error = e.message) }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { state ->
            val filtered = if (query.isNotBlank()) {
                state.allNpcs.filter { n ->
                    n.name.contains(query, ignoreCase = true) ||
                        n.type.name.contains(query, ignoreCase = true) ||
                        n.location.contains(query, ignoreCase = true) ||
                        n.description?.contains(query, ignoreCase = true) == true
                }
            } else {
                state.allNpcs
            }
            state.copy(
                npcs = filtered,
                searchQuery = query,
                isLoading = false,
                error = null
            )
        }
    }
}
