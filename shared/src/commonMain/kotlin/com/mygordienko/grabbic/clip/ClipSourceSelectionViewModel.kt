package com.mygordienko.grabbic.clip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mygordienko.grabbic.clip.domain.ClipSource
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

class ClipSourceSelectionViewModel(
    val repository: ClipSourceRepository
): ViewModel() {

    private val _state = MutableStateFlow<ClipSourceSelectionScreenState>(ClipSourceSelectionScreenState())
    val state: StateFlow<ClipSourceSelectionScreenState> = _state

    init {
        viewModelScope.launch {
            doLoadSources()
        }
    }

    fun addSource(source: ClipSource) {
        viewModelScope.launch {
            repository.addSource(source)
            doLoadSources()
        }
    }

    fun deleteSource(source: ClipSource) {
        viewModelScope.launch {
            repository.deleteSource(source.id)
            doLoadSources()
        }
    }

    private suspend fun doLoadSources() {
        _state.value = _state.value.copy(isLoading = true, sources = emptyList())
        try {
            val sources = repository.getAllSources()
            _state.value = _state.value.copy(isLoading = false, sources = sources)
        } catch (_: Exception) {
            _state.value = _state.value.copy(isLoading = false, sources = emptyList())
        }
    }
}

data class ClipSourceSelectionScreenState(
    val isLoading: Boolean = false,
    val sources: List<ClipSource> = emptyList()
)