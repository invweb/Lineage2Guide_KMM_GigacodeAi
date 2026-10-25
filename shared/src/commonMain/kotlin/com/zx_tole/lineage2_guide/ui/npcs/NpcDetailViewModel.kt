package com.zx_tole.lineage2_guide.ui.npcs

import com.zx_tole.lineage2_guide.domain.repository.NpcsRepository
import com.zx_tole.lineage2_guide.ui.common.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class NpcDetailUiState(
    val npc: com.zx_tole.lineage2_guide.domain.model.Npc? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class NpcDetailViewModel(
    private val npcsRepository: NpcsRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(NpcDetailUiState(isLoading = true))
    val uiState: StateFlow<NpcDetailUiState> = _uiState.asStateFlow()

    fun loadNpc(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val npc = npcsRepository.getNpcById(id).first()
                if (npc != null) {
                    _uiState.update {
                        it.copy(npc = npc, isLoading = false, error = null)
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "NPC not found") }
                }
            } catch (e: Exception) {
                logError("Error in npc detail", e)
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
