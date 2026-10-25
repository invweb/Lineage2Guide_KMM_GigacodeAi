package com.zx_tole.lineage2_guide.ui.quests

import com.zx_tole.lineage2_guide.domain.model.Quest
import com.zx_tole.lineage2_guide.domain.repository.QuestsRepository
import com.zx_tole.lineage2_guide.ui.common.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class QuestsUiState(
    val quests: List<Quest> = emptyList(),
    val allQuests: List<Quest> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)

class QuestsViewModel(
    private val questsRepository: QuestsRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(QuestsUiState(isLoading = true))
    val uiState: StateFlow<QuestsUiState> = _uiState.asStateFlow()

    init {
        loadQuests()
    }

    private fun loadQuests() {
        viewModelScope.launch {
            try {
                questsRepository.getQuests(null, null, null)
                    .catch { e ->
                        logError("Error loading quests", e)
                        _uiState.update { it.copy(isLoading = false, error = e.message) }
                    }
                    .collect { quests ->
                        _uiState.update {
                            it.copy(
                                allQuests = quests,
                                quests = quests,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
            } catch (e: Exception) {
                logError("Error in quests flow", e)
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            try {
                questsRepository.getQuests(null, null, null)
                    .catch { e ->
                        logError("Error refreshing quests", e)
                    }
                    .collect { quests ->
                        val query = _uiState.value.searchQuery
                        val filtered = if (query.isNotBlank()) {
                            quests.filter { q ->
                                q.name.contains(query, ignoreCase = true) ||
                                    q.type.name.contains(query, ignoreCase = true) ||
                                    q.description?.contains(query, ignoreCase = true) == true
                            }
                        } else {
                            quests
                        }
                        _uiState.update {
                            it.copy(
                                allQuests = quests,
                                quests = filtered,
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
                state.allQuests.filter { q ->
                    q.name.contains(query, ignoreCase = true) ||
                        q.type.name.contains(query, ignoreCase = true) ||
                        q.description?.contains(query, ignoreCase = true) == true
                }
            } else {
                state.allQuests
            }
            state.copy(
                quests = filtered,
                searchQuery = query,
                isLoading = false,
                error = null
            )
        }
    }
}
