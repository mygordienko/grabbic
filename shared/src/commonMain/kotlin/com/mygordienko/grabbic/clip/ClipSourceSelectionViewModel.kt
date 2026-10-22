package com.mygordienko.grabbic.clip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mygordienko.grabbic.clip.domain.ClipSource
import jdk.jfr.internal.OldObjectSample.emit
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ClipSourceSelectionViewModel(
    val repository: ClipSourceRepository
): ViewModel() {

    /**
     * NOTE: Here we transform a cold flow we get from repository (Room's flows are cold)
     *   into a hot stateful flow using `stateIn()` this way the screen will be automatically updated
     *   each time there is an update in DB, because this hot flow will request updates.
     */
    private val _state = repository.getAllSources()
        .map { sources ->
            if (sources.isEmpty()) {
                ClipSourceSelectionScreenState(isLoading = false, sources = emptyList())
            } else {
                ClipSourceSelectionScreenState(isLoading = false, sources = sources)
            }
        }
        .catch {
            emit(ClipSourceSelectionScreenState(isLoading = false, sources = emptyList()))
        }
        .stateIn(
            scope = viewModelScope,
            // WhileSubscribed(5000) keeps the flow alive for 3 seconds during configuration changes (like rotations)
            // so it doesn't double-trigger when the screen rotates!
            started = SharingStarted.WhileSubscribed(3000),
            initialValue = ClipSourceSelectionScreenState(isLoading = true, sources = emptyList())
        )
    val state: StateFlow<ClipSourceSelectionScreenState> = _state

    fun deleteSource(source: ClipSource) {
        viewModelScope.launch {
            repository.deleteSource(source.id)
        }
    }
}

data class ClipSourceSelectionScreenState(
    val isLoading: Boolean = false,
    val sources: List<ClipSource> = emptyList()
)