package com.zx_tole.lineage2_guide.ui.skills

import com.zx_tole.lineage2_guide.domain.model.Skill
import com.zx_tole.lineage2_guide.domain.repository.SkillsRepository
import com.zx_tole.lineage2_guide.ui.common.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SkillsUiState(
    val skills: List<Skill> = emptyList(),
    val allSkills: List<Skill> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)

class SkillsViewModel(
    private val skillsRepository: SkillsRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(SkillsUiState(isLoading = true))
    val uiState: StateFlow<SkillsUiState> = _uiState.asStateFlow()

    init {
        loadSkills()
    }

    private fun loadSkills() {
        viewModelScope.launch {
            try {
                skillsRepository.refreshSkills()
                val skills = skillsRepository.getSkills(null, null, null, null).firstOrNull() ?: emptyList()
                _uiState.update {
                    it.copy(
                        allSkills = skills,
                        skills = skills,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                logError("Error loading skills", e)
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            try {
                skillsRepository.getSkills(null, null, null, null)
                    .catch { e ->
                        logError("Error refreshing skills", e)
                    }
                    .collect { skills ->
                        val query = _uiState.value.searchQuery
                        val filtered = if (query.isNotBlank()) {
                            skills.filter { s ->
                                s.name.contains(query, ignoreCase = true) ||
                                    s.classRestriction.contains(query, ignoreCase = true) ||
                                    s.type.name.contains(query, ignoreCase = true) ||
                                    s.description?.contains(query, ignoreCase = true) == true
                            }
                        } else {
                            skills
                        }
                        _uiState.update {
                            it.copy(
                                allSkills = skills,
                                skills = filtered,
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
                state.allSkills.filter { s ->
                    s.name.contains(query, ignoreCase = true) ||
                        s.classRestriction.contains(query, ignoreCase = true) ||
                        s.type.name.contains(query, ignoreCase = true) ||
                        s.description?.contains(query, ignoreCase = true) == true
                }
            } else {
                state.allSkills
            }
            state.copy(
                skills = filtered,
                searchQuery = query,
                isLoading = false,
                error = null
            )
        }
    }
}
