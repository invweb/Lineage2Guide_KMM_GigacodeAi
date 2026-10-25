package com.zx_tole.lineage2_guide.ui.classes

import com.zx_tole.lineage2_guide.domain.repository.ClassesRepository
import com.zx_tole.lineage2_guide.ui.common.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ClassDetailUiState(
    val classData: com.zx_tole.lineage2_guide.domain.model.GameClass? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class ClassDetailViewModel(
    private val classesRepository: ClassesRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ClassDetailUiState(isLoading = true))
    val uiState: StateFlow<ClassDetailUiState> = _uiState.asStateFlow()

    fun loadClass(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val classData = classesRepository.getClassById(id).first()
                if (classData != null) {
                    _uiState.update {
                        it.copy(classData = classData, isLoading = false, error = null)
                    }
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, error = "Class not found")
                    }
                }
            } catch (e: Exception) {
                logError("Error in class detail", e)
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
