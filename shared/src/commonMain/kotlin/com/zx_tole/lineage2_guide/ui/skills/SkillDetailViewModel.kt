package com.zx_tole.lineage2_guide.ui.skills

import com.zx_tole.lineage2_guide.domain.repository.SkillsRepository
import com.zx_tole.lineage2_guide.ui.common.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SkillDetailUiState(
    val skill: com.zx_tole.lineage2_guide.domain.model.Skill? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class SkillDetailViewModel(
    private val skillsRepository: SkillsRepository
) : BaseViewModel() {
    private val _uiState = MutableStateFlow(SkillDetailUiState(isLoading = true))
    val uiState: StateFlow<SkillDetailUiState> = _uiState.asStateFlow()

    fun loadSkill(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val skill = skillsRepository.getSkillById(id).first()
                if (skill != null) {
                    _uiState.update { it.copy(skill = skill, isLoading = false, error = null) }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Skill not found") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
