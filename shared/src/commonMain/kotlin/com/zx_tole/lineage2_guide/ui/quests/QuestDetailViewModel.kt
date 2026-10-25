package com.zx_tole.lineage2_guide.ui.quests

import com.zx_tole.lineage2_guide.domain.repository.QuestsRepository
import com.zx_tole.lineage2_guide.ui.common.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class QuestDetailUiState(
    val quest: com.zx_tole.lineage2_guide.domain.model.Quest? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class QuestDetailViewModel(
    private val questsRepository: QuestsRepository
) : BaseViewModel() {
    private val _uiState = MutableStateFlow(QuestDetailUiState(isLoading = true))
    val uiState: StateFlow<QuestDetailUiState> = _uiState.asStateFlow()

    fun loadQuest(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val quest = questsRepository.getQuestById(id).first()
                if (quest != null) {
                    _uiState.update { it.copy(quest = quest, isLoading = false, error = null) }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Quest not found") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
