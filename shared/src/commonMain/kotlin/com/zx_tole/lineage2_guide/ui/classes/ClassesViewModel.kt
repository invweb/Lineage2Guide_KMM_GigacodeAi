package com.zx_tole.lineage2_guide.ui.classes

import com.zx_tole.lineage2_guide.common.Logger
import com.zx_tole.lineage2_guide.domain.model.GameClass
import com.zx_tole.lineage2_guide.domain.repository.ClassesRepository
import com.zx_tole.lineage2_guide.ui.common.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ClassesUiState(
    val classes: List<GameClass> = emptyList(),
    val allClasses: List<GameClass> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)

class ClassesViewModel(
    private val classesRepository: ClassesRepository
) : BaseViewModel() {

    private val _allClasses = MutableStateFlow<List<GameClass>>(emptyList())
    private val _searchQuery = MutableStateFlow("")

    private val filteredClasses: StateFlow<List<GameClass>> = _searchQuery
        .combine(_allClasses) { query, classes ->
            if (query.isBlank()) classes else classes.filter { c ->
                c.name.contains(query, ignoreCase = true) ||
                    c.race.contains(query, ignoreCase = true) ||
                    c.subClasses.any { sub -> sub.contains(query, ignoreCase = true) }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val uiState: StateFlow<ClassesUiState> = filteredClasses
        .map { classes ->
            ClassesUiState(
                classes = classes,
                allClasses = _allClasses.value,
                isLoading = false,
                isRefreshing = false,
                error = null,
                searchQuery = _searchQuery.value
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ClassesUiState(isLoading = true))

    init {
        loadClasses()
    }

    private fun loadClasses() {
        viewModelScope.launch {
            try {
                classesRepository.refreshClasses()
                _allClasses.value = classesRepository.getClasses(null, null).firstOrNull() ?: emptyList()
            } catch (e: Exception) {
                logError("Error loading classes", e)
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            classesRepository.refreshClasses()
            loadClasses()
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}
